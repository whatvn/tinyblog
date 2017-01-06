package com.pcdn.model

import java.io.File

import akka.actor.{Actor, ActorRef, Props}
import com.pcdn.model.utils.Settings
import org.mapdb.{DB, DBMaker}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by Hung on 9/13/16.
  */
object Database extends Settings {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val dbFile = new File("%s/%s/%s".format(dataDir, "db", "blog"))
  val dbDirectory: Boolean = new File("%s/%s".format(dataDir, "db")).mkdir()
  val db = DBMaker.fileDB(dbFile)
    .closeOnJvmShutdown()
    .checksumStoreEnable()
    .fileMmapEnable().make()
  val commitActor: ActorRef = TinyActor.getSystem().actorOf(Props(new DBCommitter(db)), name = "dbcommiter")
  TinyActor.getSystem().scheduler.schedule(2 seconds, 60 seconds, commitActor, "commit")

  private class DBCommitter(database: DB) extends Actor {
    override def receive = {
      case "commit" =>
        database.commit()
        TinyActor.getSystem().log.info("[mapdb] Database committer has persisted data to disk.")
    }
  }

}
