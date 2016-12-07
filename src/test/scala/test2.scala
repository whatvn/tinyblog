//import java.net.URI
//
//import akka.io.IO
//import spray.can.Http
//import spray.client.pipelining._
//import akka.pattern.ask
//import scala.concurrent.Future
//
///**
//  * Created by Hung on 10/3/16.
//  */
//
//object test2 {
//
//  def main(args: Array[String]): Unit = {
//
//    var pls: Option[Future[SendReceive]] = None
//    def createPipeLine(host: String) = {
//      val pipeLine: Future[SendReceive] = for (
//        Http.HostConnectorInfo(connector, _) <-
//        IO(Http) ? Http.HostConnectorSetup(host, port = 443)
//      ) yield sendReceive(connector)
//      pls = Some(pipeLine)
//      pipeLine
//    }
//
//    val uri = new URI("http://google.com/?id=3")
//    println(uri.getHost)
//    println(uri.getPath)
//    println(uri.getQuery)
//
//    val host = "http://google.com"
//    val request = Get("/path")
//
//
//    val responseFuture: Future[SendReceive] = pls.getOrElse(createPipeLine(host))
//    /*
//    use responseFuture...
//     */
//
//  }
//}
