package com.pcdn.controller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import com.pcdn.model.About
import com.pcdn.model.utils.HttpHeaders

import scala.language.postfixOps

/**
  * Created by Hung on 9/12/16.
  */
trait AboutPage extends Directives with SprayJsonSupport {

  import com.pcdn.model.github.JsonConversion.aboutFormat

  lazy val aboutPage: Route = get {
    path("about") {
      encodeResponse {
        respondWithHeaders(HttpHeaders.expires) {
          complete(About.buildAboutPage())
        }
        //        Producer.response(ContentTypes.`text/html(UTF-8)`, html.about.render toString )
      }
    }
  }
}
