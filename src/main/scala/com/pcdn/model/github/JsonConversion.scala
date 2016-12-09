package com.pcdn.model.github

import spray.json.{DefaultJsonProtocol, RootJsonFormat}
/**
  * Created by Hung on 8/19/16.
  */
object JsonConversion extends DefaultJsonProtocol {
  implicit val errorMessageFormat: RootJsonFormat[BadCredentials] = jsonFormat2(BadCredentials)
  implicit val authorToJson: RootJsonFormat[author] = jsonFormat3(author)
  implicit val commitInfoToJson: RootJsonFormat[commitInfo] = jsonFormat(commitInfo, "author")
  implicit val commitToJson: RootJsonFormat[commit] = jsonFormat(commit, "url")
  implicit val fileDetailToJson: RootJsonFormat[fileDetail] = jsonFormat10(fileDetail)
  implicit val filesToJson: RootJsonFormat[files] = jsonFormat(files, "sha", "commit", "files")
}