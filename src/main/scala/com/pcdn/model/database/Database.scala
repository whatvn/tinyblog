package com.pcdn.model.database

import com.pcdn.model.Database.db
import org.mapdb.serializer.GroupSerializer
import org.mapdb.{BTreeMap, Serializer}


/**
  * Created by Hung on 1/6/17.
  */

abstract class Database[T] {


  def get(k: String): T

  def getAll: List[String]

  def put(k: String, v: T): Unit

  def isSet(k: String): Boolean

  def size: Long

  def replace(k: String, v: T): Unit

  def remove(k: String): Unit

  def drop: Unit
}

object MapDB {
  def apply[T, V <: GroupSerializer[T]](name: String, v: V): Database[T] = {
    val engine = db.treeMap(name).keySerializer(Serializer.STRING).
      valueSerializer(v).
      counterEnable().
      createOrOpen()
    new MapDB[T](engine)
  }

  private class MapDB[T](val engine: BTreeMap[String, T]) extends Database[T] {

    override def getAll: List[String] = engine.getKeys.toArray.map(_.toString).toList

    override def isSet(k: String): Boolean = engine.containsKey(k)

    override def size: Long = engine.size()

    override def remove(k: String): Unit = engine.remove(k)

    override def get(k: String): T = engine.get(k)

    override def put(k: String, v: T): Unit = engine.put(k, v)

    override def replace(k: String, v: T): Unit = engine.replace(k, v)

    override def drop: Unit = engine.clear()
  }

}

