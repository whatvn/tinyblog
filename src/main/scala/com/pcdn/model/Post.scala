package com.pcdn.model


import spray.http.DateTime

/**
  * Created by Hung on 8/16/16.
  */
object Post {
  def get(): List[Post] = {
    Nil
  }
  def apply(title: String, url: String, content: String, date: DateTime): Post = {
    new Post(title, url, content, date)
  }
}
class Post(val title: String, val url: String, val content: String, val date: DateTime)
