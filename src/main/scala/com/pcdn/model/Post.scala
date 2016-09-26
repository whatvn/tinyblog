package com.pcdn.model


import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

import com.pcdn.model.github.BlogMetadata
import com.pcdn.model.github.BlogMetadata.BlogMetadata
import com.pcdn.model.utils.{Markdown, Settings}
import play.twirl.api.Html
import spray.http.DateTime

import scala.io.Source
import scala.language.postfixOps

/**
  * Created by Hung on 8/16/16.
  */
object Post extends Settings {
  private final val TITLE_REGEX = "TITLE\\:.*".r
  private final val DESC_REGEX = "DESCRIPTION\\:.*".r

  def apply(title: String, desc: String, content: Html, date: String, author: String): Post = {
    new Post(title, desc, content, date, author)
  }

  def listPost(id: String): Option[File] = {
    new File(dataDir + "/_posts").listFiles.find({
      x => x.getName.replaceAll(".md$", "") == id && x.isFile
    })
  }

  def listPostFromDb(page: Int): List[BlogMetadata] = {
    // Github api return newest commit then the latter, so newest key will be at the last
    BlogMetadata.listAll().reverse
  }

  def getTitle(input: String): String = TITLE_REGEX.findFirstIn(input) match {
    case None => "Untitled"
    case Some(t) => t.replace("TITLE:", "")
  }

  def getDesc(input: String): String = DESC_REGEX.findFirstIn(input) match {
    case None => ""
    case Some(d) => d.replace("DESCRIPTION:", "")
  }

  def toHtmlDocument(source: Iterator[String]) = Html(Markdown(3000).parseToHTML(source mkString "\n"))

  def getPostDay(date: String) = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault).parse(date).toString

  def buildPost(f: File): Post = {
    val resource = Source.fromFile(f, "UTF-8")
    val src: Iterator[String] = resource.getLines()
    val title = src drop 0 next
    val desc = src drop 0 next
    var (date, author) =  (DateTime.now.toIsoDateString, "Anonymous")
    val content: Html = toHtmlDocument(src)
    BlogMetadata.get("_posts/" + f.getName) match {
      case Some(x) =>
        date = getPostDay(x.updateTime)
        author = x.author
      case _ => ()
    }
    resource.close()
    Post(title.replace("TITLE:", ""), desc.replace("DESCRIPTION:", ""), content, date, author)
  }
}

class Post(val title: String, val desc: String, val content: Html, val date: String, val author: String)
