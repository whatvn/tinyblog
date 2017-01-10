package com.pcdn.model.database

import com.pcdn.model.utils.Hash
import org.mapdb.Serializer
import org.mapdb.serializer.SerializerArray


/**
  * Created by Hung on 12/13/16.
  */


object Tags extends DatabaseBrowser[Array[String]] with Hash {

  private val tagsMap: Database[Array[String]] = MapDB[Array[String], SerializerArray[String]]("tagMap", new SerializerArray[String](Serializer.STRING, classOf[String]))

  override val databaseEngine: Database[Array[String]] = tagsMap

  def update(k: String, v: String): Unit = {
    val dV: Array[String] = Array(v)
    if (isSet(k)) {
      val ov: Array[String] = get(k).get
      if (!ov.contains(v)) replace(k, dV ++ ov)
    } else
      put(k, dV)
  }

}
