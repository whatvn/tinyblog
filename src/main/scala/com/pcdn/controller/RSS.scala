package com.pcdn.controller

import com.pcdn.model.Post._
import com.pcdn.model.utils.{Render, Settings}
import spray.http.MediaTypes._
import spray.routing.HttpService
import spray.routing.directives.CachingDirectives

import scala.concurrent.duration._

/**
  * Created by Hung on 9/26/16.
  */
trait RSS extends HttpService with Render with CachingDirectives with Settings {
  lazy val rss = dynamic {
    cache(routeCache(maxCapacity = 100, initialCapacity = 10, timeToIdle = 5.seconds, timeToLive = 60.seconds)) {
      (get & path("feed.xml")) {
        renderXml(respondWithMediaType(`application/xml`), template = xml.feed.render(listPostFromDb(1), domainName))
      }
    }
  }
}
