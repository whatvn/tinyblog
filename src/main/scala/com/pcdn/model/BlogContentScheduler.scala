package com.pcdn.model

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import com.pcdn.model.github.GithubBot
import com.pcdn.model.utils.Settings

import scala.concurrent.duration.Duration

/**
  * Created by Hung on 8/30/16.
  */
object BlogContentScheduler extends Settings {

  val actorSystem = ActorSystem()

  val scheduler = actorSystem.scheduler

  val task = new Runnable {
    override def run(): Unit = {
      val crawler = GithubBot(githubUsername, githubToken, githubRepo)
      crawler.crawl()
    }
  }

  def start() = {
    actorSystem.log.info("Start crawler worker...")

    implicit val executor = actorSystem.dispatcher
    scheduler.schedule(
      initialDelay = Duration(30, TimeUnit.SECONDS),
      interval = Duration(900, TimeUnit.SECONDS),
      runnable = task)
  }
}
