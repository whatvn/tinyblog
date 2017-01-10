package com.pcdn.model.database

import org.mapdb.Serializer
import org.mapdb.serializer._

import scala.language.implicitConversions

/**
  * Created by Hung on 9/14/16.
  */

case class BlogMetadata(title: String, author: String, updateTime: String, desc: String, url: String)

case class metadataList(items: List[BlogMetadata])

object BlogMetadata extends DatabaseBrowser[Array[AnyRef]] {

  private val blogMetadata = MapDB[Array[AnyRef], SerializerArrayTuple]("blogMetadata",
    new SerializerArrayTuple(Serializer.STRING, Serializer.STRING, Serializer.STRING, Serializer.STRING, Serializer.STRING))

  override val databaseEngine: Database[Array[AnyRef]] = blogMetadata

  implicit private def anyRefToString(x: AnyRef): String = x.toString

  def getMetadata(fileId: String): Option[BlogMetadata] = {
    get(fileId) match {
      case Some(Array(title, author, updateTime, desc, url)) => Some(BlogMetadata(title, author, updateTime, desc, url))
      case _ => None
    }
  }

  def putMetadata(k: String, v: BlogMetadata) = {
    val vArray: Array[AnyRef] = Array(v.title, v.author, v.updateTime, v.desc, v.url)
    isSet(k) match {
      case false => put(k, vArray)
      case _ => replace(k, vArray)
    }
  }

  def allMetadata: List[BlogMetadata] = getAll.map {
    x =>
      blogMetadata.get(x) match {
        case Array(title, author, updateTime, desc, url) => BlogMetadata(title, author, updateTime, desc, url)
      }
  }
}

