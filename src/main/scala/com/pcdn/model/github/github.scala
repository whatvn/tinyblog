package com.pcdn.model.github

import java.io.PrintWriter

import com.github.rjeschke.txtmark._
import com.pcdn.model.utils.{HttpClient, Settings}
import spray.http.{HttpHeader, HttpResponse}
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
      val headers: List[HttpHeader] = httpResponse.headers.filter(_.lowercaseName == "link")

      headers match {
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
      val commits: List[commit] = JsonParser(httpResponse.entity.asString).convertTo[List[commit]]
      commits.foreach(commit => {
        client.process(commit.url)(filesParser)
      })
    }

    def markdownParser(httpResponse: HttpResponse): Unit = {
      val ec = Configuration.builder().setEncoding("UTF-8").build()
      val html: String = Processor.process(httpResponse.entity.asString, ec)
      println(html)
    }

    // dataDir is defined in trait Settings
    def write_file(filename: String)(httpResponse: HttpResponse): Unit = {
      val abspath = "%s/%s".format(dataDir, filename)
      httpResponse.status.intValue match {
        case 200 =>
          println("Download file: " + abspath)
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
        fileInfo.filename.endsWith(".md") && fileInfo.status != "removed"
      }).foreach(x => {
        val content_url = "https://raw.githubusercontent.com/%s/master/%s".format(repo, x.filename)
        val fileWriter = write_file(x.filename) _
        client.process(content_url)(fileWriter)
      })
    }

    def crawl(url: String = commitsUrl): Unit = {
      client.process(url)(parsePaging)
    }
  }

  def main(args: Array[String]): Unit = {
    val bot = GithubBot("whatvn", "", "whatvn/whatvn.github.io")
    bot.crawl()
  }
}
