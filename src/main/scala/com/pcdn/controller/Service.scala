package com.pcdn.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

/**
  * Created by Hung on 8/15/16.
  */
object ServiceImplement
  extends SinglePost
    with StaticResource
    with Index
    with About with RSS with Tag {
  val routes: Route = staticResources ~ indexPage ~
    singlePost ~
    aboutPage ~
    rss ~ tag

}




