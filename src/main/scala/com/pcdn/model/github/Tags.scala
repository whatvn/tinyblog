package com.pcdn.model.github

import com.pcdn.model.Database._
import com.pcdn.model.utils.Hash
import org.mapdb.serializer.SerializerArray
import org.mapdb.{BTreeMap, Serializer}

import scala.Array.concat


/**
  * Created by Hung on 12/13/16.
  */

class tagsArray(v: String) {
  def stringArr: Array[String] = Array(v)
}

object tagsArray extends Hash {
  def apply(v: String): tagsArray = new tagsArray(v)
  import scala.language.implicitConversions
  implicit def StringToArray(x: String): tagsArray = tagsArray(x)
}

object Tags extends Hash {

  private val tagsMap: BTreeMap[String, Array[String]] = db.treeMap("tagMap")
    .keySerializer(Serializer.STRING)
    .valueSerializer(new SerializerArray[String](Serializer.STRING, classOf[String]))
    .createOrOpen()

  def put(k: String, value: String)(implicit wrapByArray: String => tagsArray): Unit = {
    val hexKey = toHexString(k)
    tagsMap.containsKey(hexKey) match {
      case true => {
        val curVal = tagsMap.get(hexKey)
        if (!curVal.contains(value))
          tagsMap.put(hexKey, concat(curVal, value.stringArr))
      }
      case _ => tagsMap.put(hexKey, value.stringArr)
    }
  }

  def get(k: String): Option[Array[String]] = {
    val hexKey = toHexString(k)
    tagsMap.containsKey(hexKey) match {
      case true => Some(tagsMap.get(hexKey))
      case _ => None
    }
  }

  def getAll() {
    val a = tagsMap.getKeys.iterator()
    while (a.hasNext) println(a.next)
  }
}
