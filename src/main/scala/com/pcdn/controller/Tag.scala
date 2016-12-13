package com.pcdn.controller

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.pcdn.model.github.BlogMetadata.BlogMetadata
import com.pcdn.model.github.{BlogMetadata, Tags}
import com.pcdn.model.utils.Producer

/**
  * Created by Hung on 12/13/16.
  */
trait Tag {
  lazy val tag: Route =
    get {
      pathPrefix("tag" / RemainingPath) { id =>
          Tags.get(id.toString) match {
          case Some(p) =>
            val ms: List[BlogMetadata] = p.map(BlogMetadata.get).filter(_.isDefined).map  {
              case Some(m @unchecked) => m
            }.toList
            Producer.response(ContentTypes.`text/html(UTF-8)`, html.tag.render(ms).toString)
          case _ => redirect("/", StatusCodes.Found)
        }
      }
    }

}
