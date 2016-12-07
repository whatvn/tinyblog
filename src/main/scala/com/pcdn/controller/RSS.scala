package com.pcdn.controller

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import com.pcdn.model.Post
import com.pcdn.model.utils.{Render, Settings}

/**
  * Created by Hung on 9/26/16.
  */
trait RSS extends Settings {
  lazy val rss = {
    get {
      path("feed.xml") {
        Render.renderXml(ContentTypes.`text/xml(UTF-8)`, template = xml.feed.render(Post.listPostFromDb(1), domainName))
      }
    }
  }
}
