package org.jsun.dicely

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.jsun.dicely.mock.{ DicelyRoutesExceptionMock, DicelyRoutesMock }
import org.jsun.dicely.model.ShortenResponse
import org.scalatest.{ BeforeAndAfterEach, Matchers, WordSpec }

class DicelyAppExceptionSpec extends WordSpec with Matchers with ScalatestRouteTest
    with DicelyRoutesExceptionMock
    with BeforeAndAfterEach {

  override def beforeEach = {
    resetDB()
  }

  def routes = shortenRoute ~ geturl

  // helper
  def post(url: String): HttpRequest = {

    val jsonRequest = ByteString(
      s"""
         |{"url":"$url"}
      """.stripMargin
    )

    HttpRequest(
      HttpMethods.POST,
      uri = "/api/v1/shorten",
      entity = HttpEntity(MediaTypes.`application/json`, jsonRequest)
    )
  }

  "DicelyApp with exception" should {

    "return 200 with internal server error when exception in post" in {

      post("abc.com") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 500
        responseAs[ShortenResponse].status_text shouldBe "INTERNAL_SERVER_ERROR"
        responseAs[ShortenResponse].data shouldBe None
      }
    }

    "return 404 when exception in get" in {
      Get("/abc") ~> routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

  }

}
