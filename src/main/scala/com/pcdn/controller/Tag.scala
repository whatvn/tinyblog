package com.pcdn.controller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import com.pcdn.model.Post
import com.pcdn.model.database.metadataList
import com.pcdn.model.utils.HttpHeaders

/**
  * Created by Hung on 12/13/16.
  */
trait Tag extends Directives with SprayJsonSupport {

  import com.pcdn.model.github.JsonConversion.listBlogMetadataFormat

  lazy val tag: Route = get {
    pathPrefix("tag" / RemainingPath) { id =>
      respondWithHeaders(HttpHeaders.expires) {
        complete(metadataList(Post.retrieveTagInfo(id.toString)))
      }
      //        //            case ms: List[BlogMetadata] => Producer.response(ContentTypes.`text/html(UTF-8)`, html.tag.render(ms).toString)
    }
  }
}
