package com.pcdn.model.database

import com.pcdn.model.utils.Hash
import org.mapdb.serializer.SerializerLongArray

import scala.language.implicitConversions

/**
  * Created by Hung on 9/14/16.
  */

object CommitHistory extends Hash {

  private val commitHistory: Database[Array[Long]] = MapDB[Array[Long], SerializerLongArray]("commitHistory", new SerializerLongArray)

  def put(k: String, v: String): Unit = {
    val vArray = Array(toLong(k))
    val hexKey = toHexString(v)
    isSet(k) match {
      case true =>
        get(k) match {
          case None => commitHistory.put(k, vArray)
          case Some(ov) => commitHistory.replace(k, vArray ++ ov)
        }
      case _ => commitHistory.put(hexKey, vArray)
    }
  }

  def isSet(fileId: String): Boolean = commitHistory.isSet(toHexString(fileId))

  def get(fileId: String): Option[Array[Long]] = commitHistory.get(toHexString(fileId)) match {
    case null => None
    case x => Some(x)
  }

  def remove(fileId: String) = commitHistory.remove(toHexString(fileId))

  def getAll: List[String] = commitHistory.getAll

  def size: Long = commitHistory.size

  def isProcessed(fileId: String, sha: String): Boolean = get(fileId) match {
    case None => false
    case Some(x) => x contains toLong(sha)
  }
}
