package com.pcdn.model.utils

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ContentType.WithCharset
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.StandardRoute
import play.twirl.api.{HtmlFormat, XmlFormat}

/**
  * Created by Hung on 9/10/16.
  */
object Render {
  def renderHtml[T <: WithCharset](responseType: T, template: HtmlFormat.Appendable): StandardRoute = {
    complete(HttpEntity(responseType, template.toString))
  }

  def renderXml[T <: WithCharset](responseType: T, template: XmlFormat.Appendable): StandardRoute = {
    complete(HttpEntity(responseType, template.toString))
  }
}
