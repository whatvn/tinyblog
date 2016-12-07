package com.pcdn.model

import akka.actor.ActorSystem

/**
  * Created by Hung on 10/10/16.
  */
object TinyActor {

  implicit private val system = ActorSystem("tinyEngine")

  def getSystem() = system

}
