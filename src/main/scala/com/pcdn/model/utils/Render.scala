package com.pcdn.model.utils

import play.twirl.api.HtmlFormat
import spray.routing._

/**
  * Created by Hung on 9/10/16.
  */
trait Render extends HttpService{
  def render[T <: Directive0](responseType: T, template: HtmlFormat.Appendable): Route = responseType {
    complete(template.toString)
  }
}
