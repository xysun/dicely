package org.jsun

import akka.http.scaladsl.server.{HttpApp, Route}
import org.jsun.routes.DicelyRoutes

/**
 * Server will be started calling `WebServerHttpApp.startServer("localhost", 8080)`
 * and it will be shutdown after pressing return.
 */
object DicelyApp extends HttpApp with App with DicelyRoutes{
  def routes: Route = shorten ~ geturl
  startServer("localhost", 8080) // todo: get port from conf or cmd line
}
