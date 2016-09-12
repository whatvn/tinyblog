package com.pcdn.model


import java.io.File

import com.pcdn.model.utils.Settings
import org.clapper.markwrap.{MarkupType, MarkWrap}
import play.twirl.api.Html
import spray.http.DateTime

import scala.io.Source
import scala.language.postfixOps
/**
  * Created by Hung on 8/16/16.
  */
object Post extends Settings {
  def apply(title: String, url: String, desc: String, content: Html, date: DateTime, name: String): Post = {
    new Post(title, url, desc, content, date, name)
  }

  def listPost(): List[Post] = {
    println("calling list post")
    val posts =  new File(dataDir + "/_posts").listFiles.filter(_.isFile).map({
      f => {
        val resouce = Source.fromFile(f, "UTF-8")
        val src = resouce.getLines()
        val title = src drop 0 next
        val desc = src drop 0 next
        val content: Html = Html(MarkWrap.parserFor(MarkupType.Markdown).parseToHTML(src.mkString("\n")))
        val postName = f.getName.replaceAll(".md$", "")
        val url = "/posts/" + postName
        val date: DateTime = DateTime.now
        resouce.close()
        Post(title.replace("TITLE:", ""), url, desc.replaceAll("DESCRIPTION:", ""), content, date, postName)
      }
    }).toList
    posts
  }

}
class Post(val title: String, val url: String, val desc: String, val content: Html, val date: DateTime, val name: String)
