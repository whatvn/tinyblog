package com.pcdn.controller

import java.io.File

import com.pcdn.model.Post
import com.pcdn.model.github.GithubBot
import com.pcdn.model.utils.Settings
import play.twirl.api.HtmlFormat
import spray.http.DateTime
import spray.http.MediaTypes.{`text/html` => TEXTHTML}
import spray.routing._

import scala.io.Source

/**
  * Created by Hung on 8/16/16.
  */
trait Routes extends HttpService with Settings {
  def render[T <: Directive0](responseType: T, template: HtmlFormat.Appendable): Route = responseType {
    complete(template.toString)
  }


  def index() = {
    val gb = GithubBot("whatvn", "2c8fd5e1d6de179e2651613f9753e1b2e132f305", "vnsecurity/vnsecurity.github.io")
    gb.crawl()
  }


  def listPost(): List[Post] = {
     val posts = new File(dataDir + "/_posts").listFiles.filter(_.isFile).map({
      f => {
        val title = f.getName
        val content = Source.fromFile(f, "UTF-8").mkString("\n")
        val url = "TBA"
        val date: DateTime = DateTime.now
        Post(title, url, content, date)
      }
    }).toList
    posts
  }

  final val routes = Map(
    path("") -> getFromResourceDirectory("static"),
    pathSingleSlash -> render(respondWithMediaType(TEXTHTML), template = html.index.render(listPost))
  )
}
