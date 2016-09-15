package com.pcdn.model

/**
  * Created by Hung on 9/15/16.
  */

import akka.actor.Actor
import akka.event.Logging



case class Info(msg: String)
case class Error(msg: String)
class Logger extends Actor {
  val log = Logging(context.system, this)
  override def preStart() = {
    log.debug("Starting")
  }
  def receive = {
    case Info(x)      => log.info("{}", x)
    case Error(x)      => log.error("{}", x)
    case x            => log.debug("{}", x)
  }
}
