package org.jsun.dicely

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.jsun.dicely.mock.DicelyRoutesMock
import org.jsun.dicely.model.ShortenResponse
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class DicelyAppSpec extends WordSpec with Matchers with ScalatestRouteTest
with DicelyRoutesMock
with BeforeAndAfterEach{

  override def beforeEach = {
    resetDB()
  }

  def routes = shortenRoute ~ geturl

  "DicelyApp" should {

    "post invalid url to hash" in {
      val jsonRequest = ByteString(
        s"""
           |{"url":"abc"}
      """.stripMargin)

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/api/v1/shorten",
        entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

      postRequest ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 500
        responseAs[ShortenResponse].data shouldBe None
      }
    }

    "post valid url to hash" in {

      val jsonRequest = ByteString(
        s"""
           |{"url":"http://test1.com"}
      """.stripMargin)

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/api/v1/shorten",
        entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

      var hash = ""
      postRequest ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 200
        responseAs[ShortenResponse].data.get.new_hash shouldBe true
        hash = responseAs[ShortenResponse].data.get.hash
      }

      // post again
      postRequest ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 200
        responseAs[ShortenResponse].data.get.new_hash shouldBe false
        responseAs[ShortenResponse].data.get.hash shouldBe hash
      }

    }

    "get a hashed short url should return redirect" in {

      val jsonRequest = ByteString(
        s"""
           |{"url":"http://test1.com"}
      """.stripMargin)

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/api/v1/shorten",
        entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

      var hash = ""
      postRequest ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[ShortenResponse].status_code shouldBe 200
        responseAs[ShortenResponse].data.get.new_hash shouldBe true
        hash = responseAs[ShortenResponse].data.get.hash
      }

      Get(s"/$hash") ~> routes ~> check {
        status shouldBe StatusCodes.MovedPermanently
        // todo: test redirect right url
      }
    }

    "get an unknown url should return 404" in {
      Get("/unknown") ~> routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }
  }

}
