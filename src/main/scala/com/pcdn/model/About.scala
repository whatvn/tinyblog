package com.pcdn.model

import java.io.File

import com.pcdn.model.utils.{Markdown, Settings}
import play.twirl.api.Html

import scala.io.{BufferedSource, Source}

/**
  * Created by Hung on 12/15/16.
  */
case class About(content: String)

object About extends Settings {
  val aboutFile = s"$dataDir/about/about.md"

  def buildAboutPage(): About = {
    val f: File = new File(aboutFile)
    if (!f.exists()) {
      new About("Nothing about me")
    } else {
      val resource: BufferedSource = Source.fromFile(f)
      val about: String = Html(Markdown(5000).parseToHTML(resource.getLines drop(1) mkString  "\n")).toString
      resource.close
      About(about)
    }
  }
}
