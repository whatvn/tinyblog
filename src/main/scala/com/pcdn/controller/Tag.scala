package com.pcdn.controller

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.pcdn.model.Post
import com.pcdn.model.github.BlogMetadata
import com.pcdn.model.utils.Producer

/**
  * Created by Hung on 12/13/16.
  */
trait Tag {
  lazy val tag: Route = get {
      pathPrefix("tag" / RemainingPath) { id =>
          Post.retrieveTagInfo(id.toString) match {
            case Nil => redirect("/", StatusCodes.Found)
            case ms: List[BlogMetadata.BlogMetadata] => Producer.response(ContentTypes.`text/html(UTF-8)`, html.tag.render(ms).toString)
        }
      }
    }
}
