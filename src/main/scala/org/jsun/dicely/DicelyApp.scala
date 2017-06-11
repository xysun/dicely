package org.jsun.dicely

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{ Directives, HttpApp, Route }
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.jsun.dicely.routes.DicelyRoutesImpl

import scala.util.Try

object DicelyApp extends Directives with DicelyRoutesImpl {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()

    implicit val executionContext = system.dispatcher
    val port = Try(args(0).toInt).getOrElse(ConfigFactory.load().getInt("port"))
    val bindingFuture = Http().bindAndHandle(routes, "localhost", port)

  }

  val routes = shortenRoute ~ geturl

}
