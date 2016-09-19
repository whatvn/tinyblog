package com.pcdn.model.utils

import akka.actor.ActorSystem
import akka.event.Logging
import akka.io.IO
import akka.pattern.ask
import spray.can.Http
import spray.client.pipelining._
import spray.http.{BasicHttpCredentials, HttpResponse}
import spray.util.pimpFuture

import scala.concurrent.duration._
import scala.util.{Failure, Success}


/**
  * Created by Hung on 8/19/16.
  */

object HttpClient {

  def apply(user: String, token: String) = new HttpClient(user, token)


  class HttpClient(val user: String, val token: String) {

    private val credential: RequestTransformer = addCredentials(BasicHttpCredentials(user, token))


    def process(url: String)(op: HttpResponse => Unit) = {
      implicit val system = ActorSystem("github-api-client")
      import system.dispatcher
      val log = Logging(system, HttpClient.getClass)
      val pipeline = sendReceive

      val responseFuture = pipeline {
        Get(url) ~> credential
      }

      responseFuture onComplete {
        case Success(response) =>  {
            op(response)
            shutdown()
        }
        case Failure(error) =>
          log.error(error, "Couldn't process url")
          shutdown()
      }

      def shutdown(): Unit = {
        IO(Http).ask(Http.CloseAll)(1.second).await
        system.shutdown()
      }
    }
  }

}

