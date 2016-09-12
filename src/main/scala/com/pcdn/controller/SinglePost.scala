package com.pcdn.controller

import com.pcdn.model.Post._
import com.pcdn.model.utils.{Render, Settings}
import spray.http.MediaTypes.{`text/html` => TEXTHTML}
import spray.routing._

import scala.language.{implicitConversions, postfixOps}

/**
  * Created by Hung on 8/16/16.
  */

trait SinglePost extends HttpService with Settings with Render {
  lazy val singlePost = (get & path("posts" / RestPath)) { id ⇒
      listPost().find(_.name == id.toString()) match {
        case Some(p) => render(respondWithMediaType(TEXTHTML), template = html.post.render(p))
        case _ => complete("Not found")
      }
  }
}
