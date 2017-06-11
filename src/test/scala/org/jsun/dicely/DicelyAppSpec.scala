package org.jsun.dicely

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.jsun.dicely.mock.DicelyRoutesMock
import org.jsun.dicely.model.ShortenResponse
import org.scalatest.{ BeforeAndAfterEach, Matchers, WordSpec }

class DicelyAppSpec extends WordSpec with Matchers with ScalatestRouteTest
    with DicelyRoutesMock
    with BeforeAndAfterEach {

  override def beforeEach = {
    getDBResource().resetDB()
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

  "DicelyApp" should {

    "get length < 3 url should return 404 immediately" in {
      Get("/ab") ~> routes ~> check {
        status shouldBe StatusCodes.NotFound
      }

      Get("/b") ~> routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "post invalid url to hash" in {

      post("abc") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 500
        responseAs[ShortenResponse].data shouldBe None
      }
    }

    "post valid url to hash" in {

      var hash = ""
      post("http://test1.com") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 200
        responseAs[ShortenResponse].data.get.new_hash shouldBe true
        hash = responseAs[ShortenResponse].data.get.hash
      }

      // post again
      post("http://test1.com") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 200
        responseAs[ShortenResponse].data.get.new_hash shouldBe false
        responseAs[ShortenResponse].data.get.hash shouldBe hash
      }

    }

    "same url should get same result -- protocol shouldn't matter" in {
      var hash = ""
      post("http://test1.com") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 200
        responseAs[ShortenResponse].data.get.new_hash shouldBe true
        hash = responseAs[ShortenResponse].data.get.hash
      }

      // post without protocol
      post("test1.com") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 200
        responseAs[ShortenResponse].data.get.new_hash shouldBe false
        responseAs[ShortenResponse].data.get.hash shouldBe hash
      }
    }

    "get a hashed short url should return redirect" in {
      val longUrl = "http://test1.com"
      var hash = ""
      post(longUrl) ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 200
        responseAs[ShortenResponse].data.get.new_hash shouldBe true
        hash = responseAs[ShortenResponse].data.get.hash
      }

      Get(s"/$hash") ~> routes ~> check {
        status shouldBe StatusCodes.MovedPermanently
        assert(responseAs[String] contains longUrl)
      }
    }

    "get an unknown url should return 404" in {
      Get("/unknown") ~> routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }
  }

}
