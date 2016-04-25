package server

import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http

object Main {
  def main(args: Array[String]): Unit = {
    println("Starting server...")

    implicit val system = ActorSystem("system")
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    Http().bindAndHandle(Routes(), "localhost", 8080)
      .map(s"Server online at http:/" + _.localAddress)
      .foreach(println)
  }
}
