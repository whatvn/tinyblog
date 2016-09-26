package com.pcdn.controller

import com.pcdn.model.Post._
import com.pcdn.model.utils.{Render, Settings}
import spray.http.MediaTypes.{`text/html` => TEXTHTML}
import spray.routing._
import spray.routing.directives.CachingDirectives

import scala.concurrent.duration._

/**
  * Created by Hung on 8/16/16.
  */

trait SinglePost extends HttpService with Settings with Render with CachingDirectives {
  lazy val singlePost = alwaysCache(routeCache(maxCapacity = 100, initialCapacity = 10, timeToLive = 30.minutes, timeToIdle = 5.seconds)) {
    (get & path("_posts" / RestPath)) { id ⇒
      listPost(id.toString) match {
        case Some(p) => renderHtml(respondWithMediaType(TEXTHTML), template = html.post.render(buildPost(p)))
        case _ => complete("Not found")
      }
    }
  }
}
