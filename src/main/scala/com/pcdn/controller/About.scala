package com.pcdn.controller

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import com.pcdn.model.utils.{Render, Settings}

/**
  * Created by Hung on 9/12/16.
  */
trait About extends Settings {

  lazy val aboutPage = get {
    path("about.html") {
      encodeResponse {
        Render.renderHtml(ContentTypes.`text/html(UTF-8)`, template = html.about.render())
      }
    }
  }
}
