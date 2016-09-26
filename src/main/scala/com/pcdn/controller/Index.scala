package com.pcdn.controller

import com.pcdn.model.Post.listPostFromDb
import com.pcdn.model.utils.Render
import spray.http.MediaTypes.{`text/html` => TEXTHTML}
import spray.routing.HttpService
import spray.routing.directives.CachingDirectives

import scala.concurrent.duration._


/**
  * Created by Hung on 9/10/16.
  */
trait Index extends HttpService with Render with CachingDirectives {

  lazy val indexPage = dynamic {
    alwaysCache(routeCache(maxCapacity = 100, initialCapacity = 10, timeToIdle = 5.seconds, timeToLive = 60.seconds)) {
      (get & pathEndOrSingleSlash) {
        renderHtml(respondWithMediaType(TEXTHTML), template = html.index.render(listPostFromDb(1)))
      }
    }
  }
}
