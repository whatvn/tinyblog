package com.pcdn.controller

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, Route}
import com.pcdn.model.Post
import com.pcdn.model.utils.{HttpHeaders, Producer, Settings}

/**
  * Created by Hung on 9/26/16.
  */
trait RSS extends Settings with Directives {
  lazy val rss: Route = get {
    path("feed.xml") {
      encodeResponse {
        respondWithHeaders(HttpHeaders.expires) {
          Producer.response(ContentTypes.`text/xml(UTF-8)`, xml.feed.render(Post.listPostFromDb(1), domainName).toString)
        }
      }
    }
  }
}
