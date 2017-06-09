package org.jsun.dicely

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives
import com.netaporter.uri.Uri.parse
import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import org.jsun.dicely.model.{JsonSupport, ShortenRequest, ShortenResponse, ShortenResult}
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}









trait DicelyRoutes extends Directives with JsonSupport{

  private val conf = ConfigFactory.load()

  private val redisClient = new RedisClient(conf.getString("redis.host"), conf.getInt("redis.port")) // todo: mock for unit test

  private val engine = new UrlShortener(redisClient)
  private val version = "v1"

  lazy val shorten =
    pathPrefix("api"){
      pathPrefix(version){
        path("shorten"){
          post{
            entity(as[ShortenRequest]) { request =>
              complete{
                Future{engine.shorten(request)}
              }
            }
          }
        }
      }
    }

  lazy val geturl =
      path(Remaining){s =>
        get{
          onComplete(Future(engine.retrieve(parse(s).pathRaw.tail))){
            case Success(v)  => v match {
              case None          => complete(HttpResponse(StatusCodes.NotFound))
              case Some(longUrl) => redirect(longUrl, StatusCodes.MovedPermanently)
            }
            case Failure(ex) => complete(HttpResponse(StatusCodes.NotFound)) // todo: logging
          }
          }
        }

}
