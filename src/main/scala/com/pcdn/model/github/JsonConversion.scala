package com.pcdn.model.github

import spray.json.DefaultJsonProtocol

/**
  * Created by Hung on 8/19/16.
  */
object JsonConversion extends DefaultJsonProtocol {

  implicit val authorToJson = jsonFormat3(author)
  implicit val commitInfoToJson = jsonFormat(commitInfo, "author")
  implicit val commitToJson = jsonFormat(commit,"sha", "commit", "url")
  implicit val fileDetailToJson = jsonFormat10(fileDetail)
  implicit val filesToJson = jsonFormat(files, "files")
}