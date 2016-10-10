package com.pcdn.model.utils

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import com.pcdn.model._
import spray.can.Http
import spray.client.pipelining._
import spray.http.{BasicHttpCredentials, HttpResponse}
import spray.util.pimpFuture

import scala.concurrent.duration._
import scala.util.{Failure, Success}


/**
  * Created by Hung on 8/19/16.
  */

object HttpClient extends Settings {

  def apply(user: String, token: String) = new HttpClient(user, token)


  class HttpClient(val user: String, val token: String) {
    implicit val system = ActorSystem("github-api-client")
    implicit val timeout = 5.seconds
    private val credential: RequestTransformer = addCredentials(BasicHttpCredentials(user, token))
//    val pipeLineMap = Map.empty[String, Future[SendReceive]]


//    def createPipeline(host: String): Future[SendReceive] =  {
//        val pipeLine = for (
//          Http.HostConnectorInfo(connector, _) <-
//          IO(Http) ? Http.HostConnectorSetup(host, port = 80)
//        ) yield sendReceive(connector)
//        pipeLineMap.put(host, pipeLine)
//        pipeLine
//    }
//
//    def getPipeline(host: String) = {
//      pipeLineMap.getOrElse(host, createPipeline(host))
//    }


    def process(url: String)(op: HttpResponse => Unit) = {

      import system.dispatcher
      val pipeline = sendReceive
      val responseFuture = pipeline {
        Get(url) ~> credential
      }
      responseFuture onComplete {
        case Success(response) => {
          Logger.logger ! s"got status: $response"
          op(response)
        }
        case Failure(error) =>
          Logger.logger ! Error(s"Couldn't process url: $error")
      }
      def shutdown(): Unit = {
        IO(Http).ask(Http.CloseAll)(5.second).await
      }
    }
    def close() = system.shutdown()
  }
}

