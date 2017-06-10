package org.jsun.dicely.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
 * Created by jsun on 6/9/2017 AD.
 */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val shortenRequestFormat = jsonFormat1(ShortenRequest)
  implicit val shortenResultFormat = jsonFormat4(ShortenResult)
  implicit val shortenResponseFormat = jsonFormat3(ShortenResponse)
}
