package server

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext

object Routes extends Directives {
  def apply()(implicit s: ActorSystem, m: Materializer, e: ExecutionContext): Route = {
    pathSingleSlash {
      getFromResource("static/index.html")
    } ~
    path(Segment) { segment =>
      getFromResource(s"static/$segment")
    } ~
    path("target" / Segment) { segment =>
      getFromResource(s"static/target/$segment")
    } ~
    path("api" / Segments) { segments =>
      post(AutowireServer.dispatch(segments))
    }
  }
}
