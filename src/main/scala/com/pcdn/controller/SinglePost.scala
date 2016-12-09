package com.pcdn.controller


import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.pcdn.model.Post
import com.pcdn.model.utils.{Render, Settings}


/**
  * Created by Hung on 8/16/16.
  */

trait SinglePost extends Settings {
  lazy val singlePost: Route =
    get {
      pathPrefix("_posts" / RemainingPath) { id =>
        Post.listPost(id.toString) match {
          case Some(p) => Render.render(ContentTypes.`text/html(UTF-8)`, html.post.render(Post.buildPost(p)).toString)
          case _ => complete("Not found")
        }
      }
    }
}
