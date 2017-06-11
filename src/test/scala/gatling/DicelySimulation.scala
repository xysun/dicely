package gatling

import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

/**
 * Created by jsun on 6/11/2017 AD.
 */
class DicelySimulation extends Simulation {

  def generateUrl(): String = {
    s"http://${UUID.randomUUID().toString}.com"
  }

  val scn = scenario("post")
    .exec(http("shorten post")
      .post("/api/v1/shorten")
      .header("Content-Type", "application/json")
      .body(StringBody(s"""{ "url": "${generateUrl()}" }""")).asJSON
      .check(status.is(200)))

  setUp(
    scn.inject(
      nothingFor(2 seconds),
      rampUsersPerSec(2) to (50) during (8 seconds),
      constantUsersPerSec(50) during (120 seconds)
    ).protocols(http.baseURL("http://localhost:8080").shareConnections)
  )

}
