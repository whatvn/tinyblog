package com.pcdn.model

import java.io.File

import akka.actor.{Actor, Props}
import com.pcdn.model.utils.Settings
import org.mapdb.{DB, DBMaker}

import scala.concurrent.duration._
/**
  * Created by Hung on 9/13/16.
  */
object Database extends Settings {

  import scala.concurrent.ExecutionContext.Implicits.global
  val dbDirectory = new File("%s/%s".format(dataDir, "db")).mkdir()
  private val dbFile = new File("%s/%s/%s".format(dataDir, "db", "blog"))

  val commitActor = TinyActor.getSystem().actorOf(Props(new DBCommitter(db)), name = "dbcommiter")

  private val commitService = TinyActor.getSystem().scheduler.schedule(2 seconds, 60 seconds, commitActor, "commit")

  val db = DBMaker.fileDB(dbFile)
    .closeOnJvmShutdown()
    .checksumStoreEnable()
    .fileMmapEnable().make()


  private class DBCommitter(database: DB) extends Actor {
    override def receive: Receive = {
      case "commit" =>
        database.commit()
        TinyActor.getSystem().log.info("[mapdb] Database committer has persisted data to disk.")
    }
  }
}
