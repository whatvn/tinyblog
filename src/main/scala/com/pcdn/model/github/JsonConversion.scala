package com.pcdn.model.github

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.pcdn.model.{About, Post}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
/**
  * Created by Hung on 8/19/16.
  */
object JsonConversion extends DefaultJsonProtocol with SprayJsonSupport  {
  implicit val errorMessageFormat: RootJsonFormat[BadCredentials] = jsonFormat2(BadCredentials)
  implicit val authorToJson: RootJsonFormat[author] = jsonFormat3(author)
  implicit val commitInfoToJson: RootJsonFormat[commitInfo] = jsonFormat(commitInfo, "author")
  implicit val commitToJson: RootJsonFormat[commit] = jsonFormat(commit, "url")
  implicit val fileDetailToJson: RootJsonFormat[fileDetail] = jsonFormat10(fileDetail)
  implicit val filesToJson: RootJsonFormat[files] = jsonFormat(files, "sha", "commit", "files")
  implicit val blogMetadataFormat: RootJsonFormat[BlogMetadata] = jsonFormat5(BlogMetadata.apply)
  implicit val listBlogMetadatFormat: RootJsonFormat[metadataList] = jsonFormat1(metadataList)
  implicit val postFormat: RootJsonFormat[Post] = jsonFormat6(Post.apply)
  implicit val aboutFormat: RootJsonFormat[About] = jsonFormat1(About.apply)
}