package gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

/**
 * Created by jsun on 6/11/2017 AD.
 */
class DicelySimulation extends Simulation {

  val generateUrl = Iterator.continually(
    Map("URLRequest" -> s"http://www.abc${Random.nextInt(Int.MaxValue)}.com")
  )

  val scn = scenario("post")
    .feed(generateUrl)
    .exec(http("shorten post")
      .post("/api/v1/shorten")
      .header("Content-Type", "application/json")
      .body(StringBody("""{ "url": "${URLRequest}" }""")).asJSON
      .check(status.is(200))
    )

  setUp(
    scn.inject(
      nothingFor(2 seconds),
      rampUsersPerSec(2) to (1000) during (8 seconds),
      constantUsersPerSec(1000) during (120 seconds)
    ).protocols(http.baseURL("http://localhost:8080").shareConnections)
  )

}
