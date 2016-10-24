package com.pcdn.model.utils

/**
  * Created by Hung on 8/29/16.
  */
import com.pcdn.model.TinyActor
import com.typesafe.config.{Config, ConfigFactory}
import spray.httpx.encoding.{Deflate, Gzip, NoEncoding}
import spray.routing.directives.CompressResponseMagnet

trait Settings {
  implicit val system = TinyActor.getSystem()
  final val configLoader: Config = ConfigFactory.load()
  final val dataDir: String = configLoader.getString("blog.root")
  final val githubUsername:String = configLoader.getString("blog.github.username")
  final val githubToken:String = configLoader.getString("blog.github.token")
  final val githubRepo:String = configLoader.getString("blog.github.repo")
  final val domainName: String = configLoader.getString("blog.domain")
  def compressResponseMagnet: CompressResponseMagnet = CompressResponseMagnet.fromEncoders3((Gzip, Deflate, NoEncoding))
}
