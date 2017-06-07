package org.jsun.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import org.jsun.UrlShortener
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by jsun on 6/6/2017 AD.
  *
  * routes:
  *
  * POST /api/v1/shorten
  * request: {"url":"xxx"}
  * response:
  * {
      "status_code": 200,
      "data": {
        "url": "http://bit.ly/2scqcO2",
        "long_url": "https://mvnrepository.com/artifact/org.json4s/json4s-jackson_2.11/3.5.2",
        "hash": "2scsNYh",
        "is_new_hash": 1
      },
      "status_txt": "OK"
    }
  *
  * GET /abcde -> 304 redirect
  *
  *
  */

final case class ShortenRequest(url:String)
final case class ShortenResult(
                              url:String,
                              long_url:String,
                              hash:String,
                              new_hash:Boolean
                              )
final case class ShortenResponse(
                                status_code:Int,
                                status_text:String,
                                data:ShortenResult
                                )

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val shortenRequestFormat  = jsonFormat1(ShortenRequest)
  implicit val shortenResultFormat   = jsonFormat4(ShortenResult)
  implicit val shortenResponseFormat = jsonFormat3(ShortenResponse)
}

trait DicelyRoutes extends Directives with JsonSupport{

  private val engine = new UrlShortener

  lazy val shorten =

    pathPrefix("api"){
      pathPrefix("v1"){
        path("shorten"){
          post{
            entity(as[ShortenRequest]) { request =>
              complete{
                Future{
                  engine.shorten(request)
                }
              }
            }
          }
        }
      }
    }

  lazy val geturl = // todo: make sure only one path; eg. :8080/p1/p2 should reject immediately
      path(Remaining){s =>
        get{
            redirect("http://google.com", StatusCodes.PermanentRedirect)
        }

      }
}
