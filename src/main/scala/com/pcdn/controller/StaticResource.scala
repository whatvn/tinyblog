package com.pcdn.controller

import spray.http.StatusCodes
import spray.routing.HttpService

/**
  * Created by Hung on 9/12/16.
  */
trait StaticResource extends  HttpService {
  lazy val staticResources =
    get {
      path("favicon.ico") {
        complete(StatusCodes.NotFound)
      } ~
        path("static"/Rest) { path =>
          getFromResource("static/%s" format path)
        } ~
        path("file") {
          getFromResource("application.conf")
        }
    }
}
