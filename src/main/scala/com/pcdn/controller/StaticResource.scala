package com.pcdn.controller

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.pcdn.model.utils.Settings


/**
  * Created by Hung on 9/12/16.
  */
trait StaticResource extends Settings {
  lazy val staticResources: Route =
    get {
      {
        path("favicon.ico") {
          encodeResponse {
            complete(StatusCodes.NotFound)
          }
        } ~
          pathPrefix("static" / RemainingPath) { path =>
            encodeResponse {
              println(s"Get $path")
              getFromResource("static/%s" format path)
            }
          }
      }
    }
}
