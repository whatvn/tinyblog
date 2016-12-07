package com.pcdn.controller

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.pcdn.model.Post
import com.pcdn.model.utils.{Render, Settings}

/**
  * Created by Hung on 9/10/16.
  */
trait Index extends Settings {


  lazy val indexPage: Route = {
    get {
      pathEndOrSingleSlash {
        Render.renderHtml(ContentTypes.`text/html(UTF-8)`, template = html.index.render(Post.listPostFromDb(1)))
      }
    }
  }
}
