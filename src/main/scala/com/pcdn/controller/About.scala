package com.pcdn.controller

import com.pcdn.model.utils.Render
import spray.http.MediaTypes.{`text/html` => TEXTHTML}
import spray.routing.HttpService


/**
  * Created by Hung on 9/12/16.
  */
trait About extends HttpService with Render {

  lazy val aboutPage = {
    (get & path("about.html")) {
      render(respondWithMediaType(TEXTHTML), template = html.about.render())
    }
  }
}
