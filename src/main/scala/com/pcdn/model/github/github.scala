package com.pcdn.model.github

import java.io.{File, PrintWriter}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.pcdn.model.Logger.logger
import com.pcdn.model.Post._
import com.pcdn.model.utils.HttpClient
import com.pcdn.model.{Error, Info, TinyActor}

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.{Failure, Success}


object GitHubBot {

  implicit val system = TinyActor.getSystem()
  implicit val materialize = ActorMaterializer()
  import com.pcdn.model.github.JsonConversion._
  import system.dispatcher

  def main(args: Array[String]): Unit = {
    val bot = GitHubBot("whatvn", "githubtoken", "whatvn/whatvn.github.io")
    bot.crawl()
  }

  def apply(username: String, token: String, repo: String) = {
    new GitHubBot(username, token, repo)
  }

  class GitHubBot(val username: String, val token: String, val repo: String)  {
    private final val url = "https://api.github.com/repos"
    private final val commitsUrl = "%s/%s/commits?path=_posts".format(url, repo)

    val client = HttpClient(username, token)


    def crawl(url: String = commitsUrl): Unit = {
      client.process(url)(parsePaging)
    }

    private def react[T](futureObj: Future[T])(op: T => Unit)(fop: Throwable => Unit): Unit = {
      futureObj onComplete {
        case Success(v) => op(v)
        case Failure(t) => fop(t)
      }
    }

    def logErr(t: Throwable): Unit = {
      logger ! Error(t.getMessage)
    }

    private def parsePaging(httpResponse: HttpResponse): Unit = {
      httpResponse.headers.filter(_.lowercaseName == "link") match {
        case Nil => commitsParser(httpResponse)
        case x :: Nil =>
          val paging = parseLinkHeader(x.value)
          paging.get("next") match {
            case Some(s) =>
              crawl(s)
              commitsParser(httpResponse)
            case _ => commitsParser(httpResponse)
          }
        case List(_, _) => ()
      }
    }

    private def commitsParser(r: HttpResponse) {
      val bodyFuture: Future[List[commit]] = Unmarshal(r.entity).to[List[commit]]
      react(bodyFuture)(commits =>
        commits.foreach(commit => {
          logger ! Info(commit.url)
          client.process(commit.url)(filesParser)
        }))(logErr)
    }

    private def fileWriter(path: String, content: String) {
      val f = new File(path)
      new File(f.getParent).mkdirs()
      f.delete //no matter it's exist or not
      new PrintWriter(path) {
        write(content)
        close
      }
    }

    private def write(filename: String, sha: String, updateTime: String, author: String)(r: HttpResponse): Unit = {
      val abspath = "%s/%s".format(dataDir, filename)
      r.status.intValue match {
        case 200 =>
            val url = filename.replaceAll(".md$", "")
            val bodyFuture: Future[String] = Unmarshal(r.entity).to[String]
            react(bodyFuture)(x => {
              val metadata = BlogMetadata(getTitle(x), author, updateTime, getDesc(x), url)
              BlogMetadata.put(filename, metadata)
              fileWriter(abspath, x)
              CommitHistory.update(filename, sha)
            })(logErr)
        case x: Int => logger ! Error(r.headers.toString)
      }
    }

    private def parseLinkHeader(linkHeader: String): Map[String, String] = (linkHeader.split(',') map { part: String =>
      val section = part.split(';')
      val url = section(0).replace("<", "").replace(">", "")
      val name = section(1).replace(" rel=", "")
      name -> url
    }).toMap

    private def filesParser(r: HttpResponse) {
      val bodyFuture: Future[files] = Unmarshal(r.entity).to[files]
      react(bodyFuture)(files => {
        files.details.filter(fileInfo => {
          // TODO: remove file marked as removed
          fileInfo.filename.endsWith(".md") && fileInfo.status != "removed"
        }).foreach(x => {
          CommitHistory.isProcessed(x.filename, files.sha) match {
            case false =>
              val content_url = "https://raw.githubusercontent.com/%s/master/%s".format(repo, x.filename)
              val writer = write(x.filename, files.sha, files.commit.author.date, files.commit.author.name) _
              client.process(content_url)(writer)
            case _ => logger ! Info(s"${files.sha} already processed")
          }
        })
      })(logErr)
    }
  }
}
