package com.pcdn.model.github

import java.io.PrintWriter

import akka.actor.{Props, ActorRef, ActorSystem}
import com.github.rjeschke.txtmark._
import com.pcdn.model.Post._
import com.pcdn.model.Logger
import com.pcdn.model.Error
import com.pcdn.model.utils.{HttpClient, Settings}
import spray.http.HttpResponse
import spray.json.{JsValue, JsonParser}

import scala.language.implicitConversions


object GithubBot {


  //  private val sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault)

  def apply(username: String, token: String, repo: String) = {
    new GithubBot(username, token, repo)
  }

  class GithubBot(val username: String, val token: String, val repo: String) extends Settings {

    private final val url = "https://api.github.com/repos"
    private final val commitsUrl = "%s/%s/commits?path=_posts".format(url, repo)
    val client = HttpClient(username, token)
    import JsonConversion._


    private val logger:ActorRef = ActorSystem.create.actorOf(Props[Logger])

    private def parsePaging(httpResponse: HttpResponse): Unit = {
      httpResponse.headers.filter(_.lowercaseName == "link") match {
        case Nil => commitsParser(httpResponse)
        case x :: Nil => {
          val paging = parseLinkHeader(x.value)
          paging.get("next") match {
            case Some(s) => {
              crawl(s)
              commitsParser(httpResponse)
            }
            case _ => commitsParser(httpResponse)
          }
        }
        case List(_, _) => ()
      }
    }

    private def commitsParser(httpResponse: HttpResponse): Unit = {
      try {
        val commits: List[commit] = JsonParser(httpResponse.entity.asString).convertTo[List[commit]]
        commits.foreach(commit => {
          client.process(commit.url)(filesParser)
        })
      } catch {
        case de: spray.json.DeserializationException =>
          val errorMsg = JsonParser(httpResponse.entity.asString).convertTo[BadCredentials]
          logger!Error(s"${errorMsg.message}, read ${errorMsg.documentation_url}" )
      }
    }

    def markdownParser(httpResponse: HttpResponse): Unit = {
      val ec = Configuration.builder().setEncoding("UTF-8").build()
      val html: String = Processor.process(httpResponse.entity.asString, ec)
      println(html)
    }

    //    // dataDir is defined in trait Settings
    //    def writeFile(filename: String)(httpResponse: HttpResponse): Unit = {
    //      val abspath = "%s/%s".format(dataDir, filename)
    //      httpResponse.status.intValue match {
    //        case 200 =>
    //          new PrintWriter(abspath) {
    //            write(httpResponse.entity.asString)
    //            close()
    //          }
    //        case _ =>
    //      }
    //    }


    def write(filename: String, sha: String, updateTime: String, author: String)(httpResponse: HttpResponse): Unit = {
      CommitHistory.update(filename, sha)
      val abspath = "%s/%s".format(dataDir, filename)
      httpResponse.status.intValue match {
        case 200 =>
          val url = filename.replaceAll(".md$", "")
          val responseString = httpResponse.entity.asString
          val metadata = BlogMetadata(getTitle(responseString), author, updateTime, getDesc(responseString), url)
          BlogMetadata.put(filename, metadata)
          new PrintWriter(abspath) {
            write(httpResponse.entity.asString)
            close()
          }
        case _ =>
      }
    }


    private def parseLinkHeader(linkHeader: String): Map[String, String] = (linkHeader.split(',') map { part: String =>
      val section = part.split(';')
      val url = section(0).replace("<", "").replace(">", "")
      val name = section(1).replace(" rel=", "")
      name -> url
    }).toMap

    private def filesParser(httpResponse: HttpResponse): Unit = {
      val jsonResult: JsValue = JsonParser(httpResponse.entity.asString)
      val files = jsonResult.convertTo[files]
      files.details.filter(fileInfo => {
        // TODO: remove file marked as removed
        fileInfo.filename.endsWith(".md") && fileInfo.status != "removed"
      }).foreach(x => {
        val b = CommitHistory.isProcessed(x.filename, files.sha)
        if (!b) {
          val content_url = "https://raw.githubusercontent.com/%s/master/%s".format(repo, x.filename)
          println(files.commit.author.toString)
          println(files.commit.author.date)
          val writer = write(x.filename, files.sha, files.commit.author.date, files.commit.author.name) _
          client.process(content_url)(writer)
        }
      })
    }

    def crawl(url: String = commitsUrl): Unit = {
      client.process(url)(parsePaging)
    }
  }

  def main(args: Array[String]): Unit = {
    val bot = GithubBot("whatvn", "17c84c6efe7f6d309b28e4f7c6639de3cc8a7d17", "whatvn/whatvn.github.io")
    bot.crawl()
  }
}
