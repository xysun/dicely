package org.jsun

import akka.http.scaladsl.server.{HttpApp, Route}
import org.jsun.routes.DicelyRoutes

/**
 * Server will be started calling `WebServerHttpApp.startServer("localhost", 8080)`
 * and it will be shutdown after pressing return.
 */
object DicelyApp extends HttpApp with App with DicelyRoutes{
  // Routes that this WebServer must handle are defined here
  // Please note this method was named `route` in versions prior to 10.0.7
  def routes: Route = shorten ~ geturl

  // This will start the server until the return key is pressed
  startServer("localhost", 8080)
}
