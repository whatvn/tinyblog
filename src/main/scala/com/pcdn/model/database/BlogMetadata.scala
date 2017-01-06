package com.pcdn.model.database

import com.pcdn.model.utils.Hash
import org.mapdb.Serializer
import org.mapdb.serializer._

import scala.language.implicitConversions

/**
  * Created by Hung on 9/14/16.
  * key: stringHash(fileName)
  * value (title, author, updateTime, desc, url)
  */
case class BlogMetadata(title: String, author: String, updateTime: String, desc: String, url: String)

case class metadataList(items: List[BlogMetadata])

object BlogMetadata extends Hash {

  private val blogMetadata = MapDB[Array[AnyRef], SerializerArrayTuple]("blogMetadata", new SerializerArrayTuple(Serializer.STRING, Serializer.STRING, Serializer.STRING, Serializer.STRING, Serializer.STRING))

  implicit def anyToString(any: AnyRef): String = any.toString

  def getAll: List[BlogMetadata] = blogMetadata.getAll.map {
    x =>
      blogMetadata.get(x) match {
        case Array(title, author, updateTime, desc, url) => BlogMetadata(title, author, updateTime, desc, url)
      }
  }

  def put(k: String, v: BlogMetadata) = {
    val vArray = Array(v.title, v.author, v.updateTime, v.desc, v.url)
    val hexKey = toHexString(k)
    isSet(hexKey) match {
      case false => blogMetadata.put(k, vArray.map(_.toString))
      case _ => blogMetadata.replace(k, vArray.map(_.toString))
    }
  }

  def isSet(fileId: String): Boolean = blogMetadata.isSet(toHexString(fileId))

  def get(fileId: String): Option[BlogMetadata] = {
    blogMetadata.get(toHexString(fileId)) match {
      case Array(title, author, updateTime, desc, url) => Some(BlogMetadata(title.toString, author.toString, updateTime.toString, desc.toString, url.toString))
      case _ => None
    }
  }

  def size: Long = blogMetadata.size

  def remove(k: String): Unit = blogMetadata.remove(toHexString(k))

}

