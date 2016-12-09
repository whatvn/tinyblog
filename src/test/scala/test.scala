import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.OutgoingConnection
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Authorization
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.pcdn.model.TinyActor
import com.pcdn.model.github.commit

import scala.concurrent.Future
import scala.util.{Failure, Success}

class HttpTest  {

  def abc(): Unit = {
    val username = "whatvn"
    val token = ""
    implicit val system = TinyActor.getSystem()
    implicit val materialize = ActorMaterializer()

    import com.pcdn.model.github.JsonConversion._
    import system.dispatcher
    val authorization = List(Authorization(headers.BasicHttpCredentials(username, token)))

    val connectionFlow: Flow[HttpRequest, HttpResponse, Future[OutgoingConnection]] = Http().outgoingConnectionHttps("api.github.com")
    def op(httpResponse: HttpResponse): Unit = {
      val bodyFuture = Unmarshal(httpResponse.entity).to[List[commit]]
      bodyFuture onComplete {
        case Success(v) => v.foreach(x => println(x.url))
        case Failure(t) => println("Cannot parse list commit: " + t.getMessage)
      }
    }
    def makeRequest(uri: String, op: (HttpResponse) => Unit): Unit = {
      Source.single(HttpRequest(method = HttpMethods.GET, uri = uri, headers = authorization))
        .via(connectionFlow)
        .map(op)
        .runWith(Sink.head)
    }
    val bodyFuture = makeRequest("/repos/vnsecurity/vnsecurity.github.io/commits?path=_posts", op)
  }


}

object Test {
  def main(args: Array[String]): Unit = {
    val test = new HttpTest
    test.abc()
  }
}
