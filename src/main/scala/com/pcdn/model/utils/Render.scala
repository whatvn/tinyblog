package com.pcdn.model.utils

import play.twirl.api.{HtmlFormat, XmlFormat}
import spray.http.HttpHeaders.RawHeader
import spray.routing._

/**
  * Created by Hung on 9/10/16.
  */
trait Render extends HttpService{
  def renderHtml[T <: Directive0](responseType: T, template: HtmlFormat.Appendable): Route = responseType {
    complete(template.toString)
  }

  def renderXml[T <: Directive0](responseType: T, template: XmlFormat.Appendable): Route = responseType {
    complete(template.toString.trim)
  }
}
