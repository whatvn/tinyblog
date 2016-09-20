package com.pcdn.model.utils

import org.pegdown.{Extensions, PegDownProcessor}

/**
  * Created by Hung on 9/20/16.
  */
case class Markdown(maxParsingTime: Long = 2000) {
  val pegDown = new PegDownProcessor(Extensions.NONE |
    Extensions.DEFINITIONS |
    Extensions.TABLES |
    Extensions.FENCED_CODE_BLOCKS, maxParsingTime)

  def parseToHTML(source: String): String = {
    pegDown.markdownToHtml(source)
  }
}
