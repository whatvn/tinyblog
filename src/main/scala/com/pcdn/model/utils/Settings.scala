package com.pcdn.model.utils

/**
  * Created by Hung on 8/29/16.
  */
import com.typesafe.config.{Config, ConfigFactory}

trait Settings {
  final val configLoader: Config = ConfigFactory.load()
  final val dataDir: String = configLoader.getString("blog.root")
  final val githubUsername:String = configLoader.getString("blog.github.username")
  final val githubToken:String = configLoader.getString("blog.github.token")
  final val githubRepo:String = configLoader.getString("blog.github.repo")
}
