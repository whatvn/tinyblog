package com.pcdn.controller


import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import com.pcdn.model.Post
import com.pcdn.model.utils.HttpHeaders


/**
  * Created by Hung on 8/16/16.
  */

trait SinglePost extends Directives with SprayJsonSupport {

  import com.pcdn.model.github.JsonConversion.postFormat

  lazy val singlePost: Route =
    get {
      pathPrefix("_posts" / RemainingPath) { id =>
        Post.listPost(id.toString) match {
          //          case Some(p) => Producer.response(ContentTypes.`text/html(UTF-8)`, html.post.render(Post.buildPost(p)).toString)
          case Some(p) => encodeResponse {
            respondWithHeaders(HttpHeaders.expires) {
              complete(Post.buildPost(p))
            }
          }
          case _ => complete("Not found")
        }
      }
    }
}
