package com.pcdn.model.utils

import akka.http.scaladsl.model.ContentType.WithCharset
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute

/**
  * Created by Hung on 9/10/16.
  */
object Producer {
  def response[T <: WithCharset](responseType: T, template: String): StandardRoute = {
    responseType match {
      case ContentTypes.`text/html(UTF-8)` => complete(HttpEntity(responseType, template))
      case _ => complete(HttpEntity(responseType, template.trim))
    }
  }
}
