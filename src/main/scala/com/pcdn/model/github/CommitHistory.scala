package com.pcdn.model.github

import com.pcdn.model.Database._
import org.mapdb.serializer.SerializerLongArray
import org.mapdb.{BTreeMap, Serializer}
import scala.language.implicitConversions


import scala.util.hashing.MurmurHash3.stringHash

/**
  * Created by Hung on 9/14/16.
  * commit history is a mapdb tree map with key is
  * hash (filename) and value is array of sha commit
  * when a new commit is retrieve, bot will check if commit sha is available in this array
  * if it's not available download file, put commit sha to this map
  */
object CommitHistory {

  implicit def shaStringToMurmurHash(sha: String): Long = stringHash(sha).toLong
  private val commitHistory: BTreeMap[String, Array[Long]] = db.treeMap("commitHistory"). keySerializer(Serializer.STRING).
    valueSerializer(new SerializerLongArray()).
    counterEnable().createOrOpen()


  def update(fileId: String, sha: String) = {
    get(fileId) match {
      case None => commitHistory.put(fileId, Array(sha))
      case Some(x) if !x.contains(sha) =>
        commitHistory.replace(fileId, Array(shaStringToMurmurHash(sha)) ++ x)
      case _ => ()
    }

  }

  def get(fileId: String): Option[Array[Long]] = {
   commitHistory.get(fileId) match {
     case null => None
     case x => Some(x)
   }

  }

}
