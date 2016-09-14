package com.pcdn.model.github

import com.pcdn.model.Database._
import org.mapdb.{BTreeMap, Serializer}

/**
  * Created by Hung on 9/14/16.
  */
object BlogContent {

  private val blogContent: BTreeMap[String, String] = db.treeMap("blogContent"). keySerializer(Serializer.STRING).
    valueSerializer(Serializer.STRING).
    counterEnable().createOrOpen()


  def put(fileId: String, content: String): Unit = {
    blogContent.replace(fileId, content)
  }

  def get(fileId: String): String = {
    blogContent.get(fileId)
  }
}
