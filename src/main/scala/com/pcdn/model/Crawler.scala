package com.pcdn.model

import akka.actor.{Actor, ActorSystem, Props}
import com.pcdn.model.github.GithubBot
import com.pcdn.model.utils.Settings
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by Hung on 8/30/16.
  */
object Crawler extends Settings {

  val actorSystem = ActorSystem()
  val ghActor = ActorSystem().actorOf(Props(new Crawler), "crawler")
  val scheduler = actorSystem.scheduler.schedule(2 seconds, 60 seconds, ghActor, "crawl")

  val bot = GithubBot(githubUsername, githubToken, githubRepo)

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
