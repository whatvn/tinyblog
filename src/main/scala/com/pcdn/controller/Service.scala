package com.pcdn.controller

import akka.actor.Actor
import akka.event.Logging
import spray.http.HttpRequest
import spray.routing.directives.LogEntry

import scala.language.implicitConversions

/**
  * Created by Hung on 8/15/16.
  */
class ServiceImplement extends Actor
  with SinglePost
  with StaticResource
  with Index
  with About with RSS {

  override def actorRefFactory = context

  val log = Logging(context.system, this)

  def showPath(req: HttpRequest) = LogEntry("Method = %s, Path = %s" format(req.method, req.uri), Logging.DebugLevel)

  override def receive: Receive = runRoute {
    logRequest(showPath _)(
      staticResources ~
        indexPage ~
        singlePost ~
        aboutPage ~
        rss
    )
  }
}




