package com.pcdn.controller

import com.pcdn.model.utils.{Render, Settings}
import spray.http.MediaTypes.{`text/html` => TEXTHTML}
import spray.routing.HttpService


/**
  * Created by Hung on 9/12/16.
  */
trait About extends HttpService with Render with Settings {

  lazy val aboutPage = (get & compressResponse(compressResponseMagnet) & path("about.html")) {
    renderHtml(respondWithMediaType(TEXTHTML), template = html.about.render())
  }
}
