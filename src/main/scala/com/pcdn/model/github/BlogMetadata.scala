package com.pcdn.model.github

import com.pcdn.model.Database._
import com.pcdn.model.utils.Hash
import org.mapdb.serializer._
import org.mapdb.{BTreeMap, Serializer}
import play.twirl.api.TemplateMagic.javaCollectionToScala

import scala.language.implicitConversions

/**
  * Created by Hung on 9/14/16.
  * key: stringHash(fileName)
  * value (title, author, updateTime, desc, url)
  */
object BlogMetadata {

  implicit def anyToString(any: AnyRef) = any.toString


  private val blogMetadata: BTreeMap[String, Array[AnyRef]] = db.treeMap("blogMetadata").keySerializer(Serializer.STRING).
    valueSerializer(new SerializerArrayTuple(Serializer.STRING, Serializer.STRING, Serializer.STRING, Serializer.STRING, Serializer.STRING)).
    counterEnable().createOrOpen()

  def listAll(): List[BlogMetadata] = {
    blogMetadata.getKeys.toList.map {
      x =>
        blogMetadata.get(x) match {
          case Array(title, author, updateTime, desc, url) => BlogMetadata(title, author, updateTime, desc, url)
        }
    }
  }

  def apply(title: String, author: String, updateTime: String, desc: String, url: String) = {
    new BlogMetadata(title, author, updateTime, desc, url)
  }

  def put(fileId: String, metadata: BlogMetadata) = {
    val v = Array(metadata.title, metadata.author, metadata.updateTime, metadata.desc, metadata.url)
    val k = Hash.toHexString(fileId)
    isSet(fileId) match {
      case false => blogMetadata.put(k, v.map(_.toString))
      case _ => blogMetadata.replace(k, v.map(_.toString))
    }
  }

  def isSet(fileId: String): Boolean = blogMetadata.containsKey(Hash.toHexString(fileId))

  def get(fileId: String): Option[BlogMetadata] = {
    blogMetadata.get(Hash.toHexString(fileId)) match {
      case Array(title, author, updateTime, desc, url) => Some(BlogMetadata(title.toString, author.toString, updateTime.toString, desc.toString, url.toString))
      case _ => None
    }
  }

  def size() = blogMetadata.getSize

  // companion object & class
  class BlogMetadata(val title: String, val author: String, val updateTime: String, val desc: String, val url: String)

}
