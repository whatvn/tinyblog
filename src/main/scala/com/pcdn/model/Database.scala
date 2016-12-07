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
  private val dbFile = new File("%s/%s/%s".format(dataDir, "db", "blog"))
  val dbDirectory = new File("%s/%s".format(dataDir, "db")).mkdir()
  val db = DBMaker.fileDB(dbFile)
    .closeOnJvmShutdown()
    .checksumStoreEnable()
    .fileMmapEnable().make()
  val commitActor = TinyActor.getSystem().actorOf(Props(new DBCommitter(db)), name = "dbcommiter")
  private val commitService = TinyActor.getSystem().scheduler.schedule(2 seconds, 60 seconds, commitActor, "commit")

  private class DBCommitter(database: DB) extends Actor {
    override def receive: Receive = {
      case "commit" =>
        database.commit()
        TinyActor.getSystem().log.info("[mapdb] Database committer has persisted data to disk.")
    }
  }

}
