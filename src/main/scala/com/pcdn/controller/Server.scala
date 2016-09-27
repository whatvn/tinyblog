package com.pcdn.controller

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.pcdn.model.Crawler
import spray.can.Http

import scala.concurrent.duration._

/**
  * Created by Hung on 8/15/16.
  */
object Server {

  def main(args: Array[String]) {

    // start worker to update blog content
    Crawler.start

    implicit val system = ActorSystem("tinyEngine")

    lazy val service = system.actorOf(Props[ServiceImplement], "blogService")

    implicit val timeout = Timeout(5.seconds)

    IO(Http)? Http.Bind(service, interface = System.getProperty("listen", "127.0.0.1"),
      port =  System.getProperty("serverport", "8080").toInt)
  }

}
