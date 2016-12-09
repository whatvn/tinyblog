package com.pcdn.controller

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.pcdn.model.utils.{Producer, Settings}
import scala.language.postfixOps
/**
  * Created by Hung on 9/12/16.
  */
trait About extends Settings {

  lazy val aboutPage: Route = get {
    path("about.html") {
      encodeResponse {
        Producer.response(ContentTypes.`text/html(UTF-8)`, html.about.render toString )
      }
    }
  }
}
