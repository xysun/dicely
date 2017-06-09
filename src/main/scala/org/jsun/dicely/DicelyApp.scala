package org.jsun.dicely

import akka.http.scaladsl.server.{ HttpApp, Route }
import com.typesafe.config.ConfigFactory
import org.jsun.dicely.routes.DicelyRoutesImpl

/**
 * Server will be started calling `WebServerHttpApp.startServer("localhost", 8080)`
 * and it will be shutdown after pressing return.
 */
object DicelyApp extends HttpApp with App with DicelyRoutesImpl {

  def routes: Route = shortenRoute ~ geturl

  val port = ConfigFactory.load().getInt("port")

  startServer("localhost", port)
}
