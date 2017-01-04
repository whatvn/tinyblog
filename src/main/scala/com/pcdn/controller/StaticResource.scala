package com.pcdn.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.pcdn.model.utils.Settings


/**
  * Created by Hung on 9/12/16.
  */
trait StaticResource extends Settings {
  lazy val staticResources: Route =
    get({
      path("favicon.ico")(getFromFile(s"$staticDir/img/favicon.ico")) ~
        pathPrefix("static" / RemainingPath)(
          path => encodeResponse(getFromFile(s"$staticDir/%s" format path))
        )
    })
}
