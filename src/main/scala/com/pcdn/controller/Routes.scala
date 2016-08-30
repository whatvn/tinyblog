package com.pcdn.controller

import com.pcdn.model.github.GithubBot
import play.twirl.api.HtmlFormat
import spray.http.MediaTypes.{`text/html` => TEXTHTML}
import spray.routing._
/**
  * Created by Hung on 8/16/16.
  */
trait Routes extends HttpService {
  def render[T <: Directive0](responseType: T , template: HtmlFormat.Appendable): Route = responseType {
    complete(template.toString)
  }

  def index() = {
    val  gb = GithubBot("whatvn", "2c8fd5e1d6de179e2651613f9753e1b2e132f305", "vnsecurity/vnsecurity.github.io")
    gb.crawl()
  }


  final val routes = Map(
    path("") -> getFromResourceDirectory("static"),
    pathSingleSlash -> render( respondWithMediaType(TEXTHTML), template = html.index.render())
  )
}
