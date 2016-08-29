package com.pcdn.controller

import akka.actor.Actor
import akka.event.Logging
import spray.http.HttpRequest
import spray.httpx.marshalling.ToResponseMarshallable
import spray.routing.Route
import spray.routing.directives.LogEntry

import scala.language.implicitConversions

/**
  * Created by Hung on 8/15/16.
  */
class ServiceImplement extends Actor with Routes {

  def actorRefFactory = context

  val log = Logging(context.system, this)

  def showPath(req: HttpRequest) = LogEntry("Method = %s, Path = %s" format(req.method, req.uri), Logging.DebugLevel)


  //
  //  implicit def makeRoute(routesMap: Map[String, HtmlFormat.Appendable]): Route =  {

  implicit def makeRoute(route:(String, Route)):Route = path(route._1) {
    route._2
  }

  implicit def makeRouteWithHtmlResponse(route:(String, ToResponseMarshallable)):Route = path(route._1) {
    get {
      complete (route._2)
    }
  }


  //    routesMap.values.tail.foldLeft(makeRoute(routesMap.head._1, routesMap.head._2)((x, y) => {
  //      x ~ makeRoute(y)
  //    })

  //    routesMap match {
  //    case (x, y) => path(x) { get
  //      {
  //        complete {
  //          y.toString
  //        }
  //      }
  //    }

  val static: Route = "static" -> getFromResourceDirectory("static")

  val router: Route = logRequest(showPath _ ) (
    routes.values.tail.foldLeft(routes.head._2)((x, y) => x ~ y)
  )
  def receive: Receive = {
    runRoute(router ~ static)
  }
}




