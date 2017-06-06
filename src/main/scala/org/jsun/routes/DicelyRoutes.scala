package org.jsun.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives
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

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val shortenRequestFormat = jsonFormat1(ShortenRequest)
}

trait DicelyRoutes extends Directives with JsonSupport{

  lazy val shorten =
    path("/api/v1/shorten"){
      post{
        entity(as[ShortenRequest]) { request =>
          complete{
            Future{
              request // todo
            }
          }
        }
      }
    }

  lazy val get = 


}
