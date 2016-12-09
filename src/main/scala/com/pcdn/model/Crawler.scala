package com.pcdn.model

import akka.actor.{Actor, Props}
import com.pcdn.model.github.GitHubBot
import com.pcdn.model.utils.Settings

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by Hung on 8/30/16.
  */
object Crawler extends Settings {

  val ghActor = TinyActor.getSystem().actorOf(Props(new Crawler), "crawler")
  val scheduler = TinyActor.getSystem().scheduler.schedule(2 seconds, 60 seconds, ghActor, "crawl")

  val bot = GitHubBot(githubUsername, githubToken, githubRepo)

  def start() = ()

  class Crawler extends Actor {
    override def preStart(): Unit = {
      Logger.logger ! Info("Github crawler started")
    }

    override def receive: Receive = {
      case "crawl" => bot.crawl()
    }
  }

}
