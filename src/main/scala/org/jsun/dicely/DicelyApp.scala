package org.jsun.dicely

import akka.http.scaladsl.server.{HttpApp, Route}
import com.typesafe.config.ConfigFactory

/**
 * Server will be started calling `WebServerHttpApp.startServer("localhost", 8080)`
 * and it will be shutdown after pressing return.
 */
object DicelyApp extends HttpApp with App with DicelyRoutes{

  def routes: Route = shorten ~ geturl

  val port = ConfigFactory.load().getInt("port")

  startServer("localhost", port)
}
