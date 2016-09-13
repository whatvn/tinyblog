package com.pcdn.model

import java.io.File
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import com.pcdn.model.utils.Settings
import org.mapdb.{DB, DBMaker}

import scala.concurrent.duration.Duration

/**
  * Created by Hung on 9/13/16.
  */
object Database extends Settings {

  private val actorSystem = ActorSystem()

  val dbDirectory = new File("%s/%s".format(dataDir, "db")).mkdir()
  private val dbFile = new File("%s/%s/%s".format(dataDir, "db", "blog"))


  val db = {
    DBMaker.fileDB(dbFile)
      .closeOnJvmShutdown()
      .fileMmapEnable().make()
  }


  private class DBCommitter(database: DB) {
    def run() = {
      database.commit()
      actorSystem.log.debug("[mapdb] Database committer has persisted data to disk.")
    }
  }

  private val task = new Runnable {
    override def run(): Unit = {
      val commiter = new DBCommitter(db)
      commiter.run
    }
  }


  def start() = {
    implicit val executor = actorSystem.dispatcher
    actorSystem.scheduler.schedule(
      initialDelay = Duration(10, TimeUnit.SECONDS),
      interval = Duration(60, TimeUnit.SECONDS),
      runnable = task)
  }
}
