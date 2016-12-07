//import akka.actor.{ActorRef, ActorSystem, Props}
//import akkapatterns.WorkPullingPattern.{Epic, RegisterWorker}
//import akkapatterns.{Master, Worker}
//import com.pcdn.model.utils.work
//import spray.http.HttpResponse
//
///**
//  * Created by Hung on 9/21/16.
//  */
//object test  {
//
//  def newEpic[T](work: T) = new Epic[T] { override def iterator = Seq(work).iterator}
//
//  def newMaster[T] = ActorSystem.create.actorOf(Props[Master[work]])
//
//  val master = newMaster
//
//
//  def doNothing(httpResponse: HttpResponse) = ()
//
//
//  def newWorker: ActorRef =  ActorSystem.create.actorOf(Props(new Worker[work](master) {
//    override def doWork(work: work): Unit = println(work.url)
//  }))
//
//
//  def main(args: Array[String]): Unit = {
//    val worker1 = newWorker
//    val worker2 = newWorker
//    val work = new work("http://google.com", doNothing)
//    master ! RegisterWorker(worker1)
//    master ! RegisterWorker(worker2)
//    master ! newEpic(work)
//    master ! newEpic(work)
////    master ! newEpic(work)
//
////    master ! KillWorker(worker)
////    master ! PoisonPill
//  }
//}
