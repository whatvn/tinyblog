package com.pcdn.model.github

/**
  * Created by Hung on 8/19/16.
  */
case class author(name: String, email: String, date: String)
case class commitInfo(author: author)
case class commit(sha: String, commit: commitInfo, url: String)