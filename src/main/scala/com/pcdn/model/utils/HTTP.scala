package com.pcdn.model.utils

import java.net.URL

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.OutgoingConnection
import akka.http.scaladsl.model.headers.Authorization
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, headers}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.pcdn.model.TinyActor

import scala.concurrent.Future


/**
  * Created by Hung on 8/19/16.
  */

object HttpClient {

  def apply(user: String, token: String) = new HttpClient(user, token)

  class HttpClient(val user: String, val token: String) {

    val authorization = List(Authorization(headers.BasicHttpCredentials(user, token)))
    implicit val system = TinyActor.getSystem()
    implicit val materialize = ActorMaterializer()


    def process(url: String)(op: HttpResponse => Unit) = {
      val fullUrl = new URL(url)
      val host = fullUrl.getHost
      val path = fullUrl.getFile
      val connectionFlow: Flow[HttpRequest, HttpResponse, Future[OutgoingConnection]] = Http().outgoingConnectionHttps(host)
      makeRequest(path, op)
      def makeRequest(uri: String, op: (HttpResponse) => Unit): Unit = {
        Source.single(HttpRequest(method = HttpMethods.GET, uri = uri, headers = authorization))
          .via(connectionFlow)
          .map(op)
          .runWith(Sink.head)
      }
    }
  }
}

