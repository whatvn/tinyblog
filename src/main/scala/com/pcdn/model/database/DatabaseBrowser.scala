package com.pcdn.model.database

import com.pcdn.model.utils.Hash

/**
  * Created by Hung on 1/9/17.
  */
abstract class DatabaseBrowser[T] extends Hash {


  val databaseEngine: Database[T]


  def get(k: String): Option[T] = databaseEngine.get(toHexString(k)) match {
    case null => None
    case x => Some(x)
  }

  // getAll will always return list keys -> List[String]
  def getAll: List[String] = databaseEngine.getAll

  def put(k: String, v: T): Unit = {
    val hexKey = toHexString(k)
    databaseEngine.put(hexKey, v)
  }

  def replace(k: String, v: T): Unit = {
    val hexKey = toHexString(k)
    databaseEngine.replace(hexKey, v)
  }

  def isSet(k: String): Boolean = databaseEngine.isSet(toHexString(k))

  def size: Long = databaseEngine.size

  def remove(k: String): Unit = databaseEngine.remove(toHexString(k))

  def clear: Unit = databaseEngine.drop
}
