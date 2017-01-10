package com.pcdn.model.utils

/**
  * Created by Hung on 8/29/16.
  */

import com.pcdn.model.TinyActor
import com.typesafe.config.{Config, ConfigFactory}

trait Settings {
  implicit val system = TinyActor.getSystem()
//  final val configLoader: Config = ConfigFactory.parseFile(new File("/Users/Hung/ScalaProjects/MyBlog/resources/application.conf"))
  final val configLoader: Config = ConfigFactory.load()
  final val dataDir: String = configLoader.getString("blog.root")
  final val staticDir: String = configLoader.getString("blog.static_dir")
  final val githubUsername: String = configLoader.getString("blog.github.username")
  final val githubToken: String = configLoader.getString("blog.github.token")
  final val githubRepo: String = configLoader.getString("blog.github.repo")
  final val domainName: String = configLoader.getString("blog.domain")
}
