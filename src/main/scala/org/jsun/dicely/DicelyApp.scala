package org.jsun.dicely

import akka.http.scaladsl.server.{HttpApp, Route}
import com.typesafe.config.ConfigFactory
import org.jsun.dicely.routes.DicelyRoutesImpl

import scala.util.Try

object DicelyApp extends HttpApp with App with DicelyRoutesImpl {

  def routes: Route = shortenRoute ~ geturl

  val port = Try(args(0).toInt).getOrElse(ConfigFactory.load().getInt("port"))


  startServer("localhost", port)
}
