package com.pcdn.controller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import com.pcdn.model.Post
import com.pcdn.model.database.metadataList
import com.pcdn.model.utils.{HttpHeaders, Settings}

/**
  * Created by Hung on 9/10/16.
  */
trait Index extends Directives with SprayJsonSupport with Settings {

  import com.pcdn.model.github.JsonConversion.{blogMetadataFormat, listBlogMetadataFormat}

  lazy val indexPage: Route = get {
    pathEndOrSingleSlash {
      //        Producer.response(ContentTypes.`text/html(UTF-8)`, html.index.render(Post.listPostFromDb(1)).toString)
      encodeResponse {
        respondWithHeaders(HttpHeaders.expires) {
          getFromFile(s"$staticDir/blog.html")
        }
      }
    }
  } ~ get {
    path("indexapi") {
      encodeResponse {
        respondWithHeaders(HttpHeaders.expires) {
          complete(metadataList(Post.listPostFromDb(1)))
        }
      }
    }
  }
}
