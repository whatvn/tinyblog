package com.pcdn.model.github

import com.pcdn.model.Database._
import com.pcdn.model.utils.Hash
import org.mapdb.serializer.SerializerLongArray
import org.mapdb.{BTreeMap, Serializer}

import scala.language.implicitConversions

/**
  * Created by Hung on 9/14/16.
  * commit history is a mapdb tree map with key is
  * hash (filename) and value is array of sha commit
  * when a new commit is retrieve, bot will check if commit sha is available in this array
  * if it's not available download file, put commit sha to this map
  */
object CommitHistory extends Hash {

  private val commitHistory: BTreeMap[String, Array[Long]] = db.treeMap("commitHistory").keySerializer(Serializer.STRING).
    valueSerializer(new SerializerLongArray()).
    counterEnable().
    createOrOpen()


  def update(fileId: String, sha: String) = {
    val v = Array(toLong(sha))
    val k = toHexString(fileId)
    isSet(fileId) match {
      case true =>
        get(fileId) match {
          case None => commitHistory.replace(k, v)
          case Some(ov) => commitHistory.replace(k, v ++ ov)
        }
      case _ => commitHistory.put(k, v)
    }
  }

  def isSet(fileId: String): Boolean = commitHistory.containsKey(toHexString(fileId))

  def get(fileId: String): Option[Array[Long]] = commitHistory.get(toHexString(fileId)) match {
    case null => None
    case x => Some(x)
  }

  def isProcessed(fileId: String, sha: String): Boolean = get(fileId) match {
    case None => false
    case Some(x) => x contains toLong(sha)
  }

  def del(fileId: String) = commitHistory.remove(toHexString(fileId))

  def listAll() = commitHistory.getKeys()
}
