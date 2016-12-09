package com.pcdn.controller

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.pcdn.model.Post
import com.pcdn.model.utils.{Render, Settings}

/**
  * Created by Hung on 9/26/16.
  */
trait RSS extends Settings {
  lazy val rss: Route = get {
    path("feed.xml") {
      Render.render(ContentTypes.`text/xml(UTF-8)`, xml.feed.render(Post.listPostFromDb(1), domainName).toString)
    }
  }
}
