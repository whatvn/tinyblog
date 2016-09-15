package com.pcdn.model


import java.io.File

import com.pcdn.model.github.BlogMetadata
import com.pcdn.model.github.BlogMetadata.BlogMetadata
import com.pcdn.model.utils.Settings
import org.clapper.markwrap.{MarkWrap, MarkupType}
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

  def apply(title: String, url: String, desc: String, content: Html, date: String, name: String): Post = {
    new Post(title, url, desc, content, date, name)
  }

  //  val system = ActorSystem()
  //  val cache: Cache[List[Post]] = LruCache(maxCapacity = 100, initialCapacity = 10, timeToIdle = 5.seconds, timeToLive = 60.seconds)

  def listPost(id: String): Option[File] = {
    new File(dataDir + "/_posts").listFiles.find({
      x => x.getName.replaceAll(".md$", "") == id && x.isFile
    })
  }


  def listPostFromDb(): List[BlogMetadata] = {
    BlogMetadata.listAll()
  }

  def getTitle(input: String): String = TITLE_REGEX.findFirstIn(input) match {
    case None => "Untitled"
    case Some(t) => t.replace("TITLE:", "")
  }

  def getDesc(input: String): String = DESC_REGEX.findFirstIn(input) match {
    case None => ""
    case Some(d) => d.replace("DESCRIPTION:", "")
  }

  // def buildPostsFromCache[T](key: T): Future[List[Post]] = cache(key) { listPost }

  def buildPost(f: File): Post = {
    val resource = Source.fromFile(f, "UTF-8")
    val src = resource.getLines()
    val title = src drop 0 next
    val desc = src drop 0 next
    val content: Html = Html(MarkWrap.parserFor(MarkupType.Markdown).parseToHTML(src.mkString("\n")))
    val postName = f.getName.replaceAll(".md$", "")
    val url = "/_posts/" + f.getName
    val date: String = BlogMetadata.get(url) match {
      case Some(x) => x.updateTime
      case _ => DateTime.now.toIsoDateString
    }
    resource.close()
    Post(title.replace("TITLE:", ""), url, desc.replace("DESCRIPTION:", ""), content, date, postName)
  }
}

class Post(val title: String, val url: String, val desc: String, val content: Html, val date: String, val name: String)
