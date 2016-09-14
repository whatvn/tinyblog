package com.pcdn.model.github

import com.pcdn.model.Database._
import org.mapdb.{BTreeMap, Serializer}
import org.mapdb.serializer._

/**
  * Created by Hung on 9/14/16.
  * key: stringHash(fileName)
  * value (title, author, updateTime)
  */
object BlogMetadata {

  private val blogMetadata: BTreeMap[String, Array[AnyRef]] = db.treeMap("blogContent").keySerializer(Serializer.STRING).
    valueSerializer(new SerializerArrayTuple(Serializer.STRING, Serializer.STRING, Serializer.STRING)).
    counterEnable().createOrOpen()


  def put(fileId: String, metadata: Array[String]) = {
      blogMetadata.replace(fileId, metadata.map(_.toString))
  }

  def get(fileId: String): Option[(String, String, String)] = {
    val b = blogMetadata.get(fileId)
    b match {
      case Array(x, y, z) => Some((x.toString, y.toString, z.toString))
      case _ => None
    }
  }
}
