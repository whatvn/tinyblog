//package com.pcdn.controller
//
//import akka.actor.{ActorRefFactory, Actor, ActorLogging}
//import akka.util.Timeout
//import com.pcdn.model.Post.listPost
//import spray.http.HttpMethods._
//import spray.http._
//
//import scala.concurrent.duration._
//
///**
//  * Created by Hung on 9/10/16.
//  */
//class BlogService extends Routes with Actor
//                with ActorLogging
//{
//  implicit val timeout: Timeout = 10.second
//
//  // for the actor 'asks'
//
//   def receive: Receive = {
//    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
//      sender ! html.index.render(listPost)
//
//    case HttpRequest(GET, Uri.Path("/static"), _, _, _) =>
//      implicit def actorRefFactory: ActorRefFactory = context
//      sender ! getFromResourceDirectory("static")
//
//    case _ =>
//      implicit def actorRefFactory: ActorRefFactory = context
//      runRoute(test)
//  }
//
//}
