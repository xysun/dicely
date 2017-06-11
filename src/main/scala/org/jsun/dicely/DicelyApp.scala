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
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher
    val port = Try(args(0).toInt).getOrElse(ConfigFactory.load().getInt("port"))
    val bindingFuture = Http().bindAndHandle(routes, "localhost", port)

  }

  // Here you can define all the different routes you want to have served by this web server
  // Note that routes might be defined in separated traits like the current case
  val routes = shortenRoute ~ geturl

}
