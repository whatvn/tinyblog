package com.pcdn.model.github

import java.io.{File, PrintWriter}

import com.pcdn.model.Error
import com.pcdn.model.Logger.logger
import com.pcdn.model.Post._
import com.pcdn.model.utils.{HttpClient, Settings}
import spray.http.HttpResponse
import spray.json.{JsValue, JsonParser}

import scala.language.implicitConversions


object GithubBot {

  def apply(username: String, token: String, repo: String) = {
    new GithubBot(username, token, repo)
  }

  class GithubBot(val username: String, val token: String, val repo: String) extends Settings {
    private final val url = "https://api.github.com/repos"
    private final val commitsUrl = "%s/%s/commits?path=_posts".format(url, repo)
    val client = HttpClient(username, token)

    import JsonConversion._

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

    private def commitsParser: (HttpResponse) => Unit = {
      httpResponse => {
        try {
          val commits: List[commit] = JsonParser(httpResponse.entity.asString).convertTo[List[commit]]
          commits.foreach(commit => {
            client.process(commit.url)(filesParser)
          })
        } catch {
          case de: spray.json.DeserializationException =>
            val errorMsg = JsonParser(httpResponse.entity.asString).convertTo[BadCredentials]
            logger ! Error(s"${errorMsg.message}, read ${errorMsg.documentation_url}")
        }
      }
    }

    private def fileWriter(path: String, content: String) = {
      val f = new File(path)
      f.delete //no matter it's exist or not
      new PrintWriter(path) {
        write(content)
        close
      }
    }

    private def write(filename: String, sha: String, updateTime: String, author: String)(httpResponse: HttpResponse): Unit = {

      val abspath = "%s/%s".format(dataDir, filename)
      httpResponse.status.intValue match {
        case 200 =>
          try {
            val url = filename.replaceAll(".md$", "")
            val responseString = httpResponse.entity.asString
            val metadata = BlogMetadata(getTitle(responseString), author, updateTime, getDesc(responseString), url)
            BlogMetadata.put(filename, metadata)
            fileWriter(abspath, httpResponse.entity.asString)
            CommitHistory.update(filename, sha)
          } catch {
            case x: Throwable => logger ! Error(x.getMessage)
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
      def parser: (fileDetail) => Unit = {
        x => {
          CommitHistory.isProcessed(x.filename, files.sha) match {
            case false =>
              val content_url = "https://raw.githubusercontent.com/%s/master/%s".format(repo, x.filename)
              val writer = write(x.filename, files.sha, files.commit.author.date, files.commit.author.name) _
              client.process(content_url)(writer)
            case _ => ()
          }
        }
      }
      files.details.filter(fileInfo => {
        // TODO: remove file marked as removed
        fileInfo.filename.endsWith(".md") && fileInfo.status != "removed"
      }).foreach(parser)
    }

    def crawl(url: String = commitsUrl): Unit = {
      client.process(url)(parsePaging)
    }
  }

  def main(args: Array[String]): Unit = {
    val bot = GithubBot("whatvn", "githubtoken", "whatvn/whatvn.github.io")
    bot.crawl()
  }
}
