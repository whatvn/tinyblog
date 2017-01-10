package com.pcdn.model.database

import org.mapdb.serializer.SerializerLongArray

import scala.language.implicitConversions

/**
  * Created by Hung on 9/14/16.
  */

object CommitHistory extends DatabaseBrowser[Array[Long]] {

  private val commitHistory: Database[Array[Long]] = MapDB[Array[Long], SerializerLongArray]("commitHistory", new SerializerLongArray)

  override val databaseEngine: Database[Array[Long]] = commitHistory


  def update(k: String, v: String): Unit = {
    val vArray = Array(toLong(v))
    isSet(k) match {
      case true =>
        get(k) match {
          case None => put(k, vArray)
          case Some(ov) => replace(k, vArray ++ ov)
        }
      case _ => put(k, vArray)
    }
  }

  def isProcessed(fileId: String, sha: String): Boolean = get(fileId) match {
    case None => false
    case Some(x) => x contains toLong(sha)
  }
}
