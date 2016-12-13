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

object tagsArray {
  def apply(v: String): tagsArray = new tagsArray(v)
  implicit def StringToArray(x: String): tagsArray = tagsArray(x)
}

object Tags extends Hash {

  private val tagsMap: BTreeMap[String, Array[String]] = db.treeMap("tagMap")
    .keySerializer(Serializer.STRING)
    .valueSerializer(new SerializerArray[String](Serializer.STRING, classOf[String]))
    .createOrOpen()

  def put[String](k: String, value: String)(implicit wrapByArray: String => tagsArray): Unit = tagsMap.containsKey(k) match {
    case true => {
      val curVal = tagsMap.get(k)
      if (!curVal.contains(value))
        tagsMap.put(k.asInstanceOf[java.lang.String], concat(curVal, value.stringArr))
    }
    case _ => tagsMap.put(k.asInstanceOf[java.lang.String], value.stringArr)
  }

  def get(k: String): Option[Array[String]] = tagsMap.containsKey(k) match {
    case true => Some(tagsMap.get(k))
    case _ => None
  }
}
