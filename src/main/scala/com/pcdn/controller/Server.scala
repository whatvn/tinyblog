package com.pcdn.controller

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.pcdn.model.{Crawler, TinyActor}

import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
  * Created by Hung on 8/15/16.
  */
object Server {

  def main(args: Array[String]) {

    // start worker to update blog content
    Crawler.start
    implicit val system = TinyActor.getSystem()
    implicit val executionContext = system.dispatcher
    implicit val materializer = ActorMaterializer()
    val host = System.getProperty("listen", "127.0.0.1")
    val port = System.getProperty("serverport", "8080").toInt
    val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(Route.handlerFlow(ServiceImplement.routes), host, port)

    bindingFuture.onComplete {
      case Success(_) => println(s"Server online at $host:$port \n")
      case Failure(t) => println(s"Server cannot start, ${t.getMessage}\n")
    }
  }
}
