package com.pcdn.controller

import com.pcdn.model.Post.listPost
import com.pcdn.model.utils.Render
import spray.http.MediaTypes.{`text/html` => TEXTHTML}
import spray.routing.HttpService


/**
  * Created by Hung on 9/10/16.
  */
trait Index extends HttpService with Render {
  lazy val indexPage = {
    (get & pathEndOrSingleSlash) {
        render(respondWithMediaType(TEXTHTML), template = html.index.render(listPost))
      }
  }
}
