package com.pcdn.model

import java.io.File

import akka.actor.{Actor, ActorSystem, Props}
import com.pcdn.model.utils.Settings
import org.mapdb.{DB, DBMaker}

import scala.concurrent.duration._
/**
  * Created by Hung on 9/13/16.
  */
object Database extends Settings {
  implicit val system = ActorSystem()
  import scala.concurrent.ExecutionContext.Implicits.global

  val dbDirectory = new File("%s/%s".format(dataDir, "db")).mkdir()
  private val dbFile = new File("%s/%s/%s".format(dataDir, "db", "blog"))

  val commitActor = system.actorOf(Props(new DBCommitter(db)), name = "dbcommiter")

  private val commitService = system.scheduler.schedule(2 seconds, 60 seconds, commitActor, "commit")

  val db = DBMaker.fileDB(dbFile)
    .closeOnJvmShutdown()
    .checksumStoreEnable()
    .fileMmapEnable().make()


  private class DBCommitter(database: DB) extends Actor {
    override def receive: Receive = {
      case "commit" =>
        database.commit()
        system.log.info("[mapdb] Database committer has persisted data to disk.")
    }
  }







}
