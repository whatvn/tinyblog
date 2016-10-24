package com.pcdn.controller

import com.pcdn.model.utils.Settings
import spray.http.StatusCodes
import spray.routing.HttpService

/**
  * Created by Hung on 9/12/16.
  */
trait StaticResource extends HttpService with Settings {
  lazy val staticResources =
    get {
      compressResponse(compressResponseMagnet) {
        path("favicon.ico") {
          complete(StatusCodes.NotFound)
        } ~
          path("static" / Rest) { path =>
            getFromResource("static/%s" format path)
          }
      }
    }
}
