package org.jsun.dicely.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives
import com.netaporter.uri.Uri.parse
import com.typesafe.scalalogging.LazyLogging
import org.jsun.dicely.UrlShortener
import org.jsun.dicely.db.{DBClient, RedisClientImpl}
import org.jsun.dicely.model.{JsonSupport, ShortenRequest}
import org.jsun.dicely.util.ResponseCreator

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

trait DicelyRoutes extends Directives with JsonSupport with UrlShortener with LazyLogging {
  this: DBClient =>

  private val version = "v1"

  lazy val shortenRoute =
    pathPrefix("api") {
      pathPrefix(version) {
        path("shorten") {
          post {
            entity(as[ShortenRequest]) { request =>
              onComplete(Future(shorten(request.url))) {
                case Success(v)  => complete(v)
                case Failure(ex) => {
                  logger.error(s"internal server error for shorten POST $request", ex)
                  complete(ResponseCreator.INTERNAL_SERVER_ERROR)
                }
              }
            }
          }
        }
      }
    }

  lazy val geturl =
    path(Remaining) { s =>
      get {
        onComplete(Future(retrieve(parse(s).pathRaw.tail))) {
          case Success(v) => v match {
            case None => complete(HttpResponse(StatusCodes.NotFound))
            case Some(longUrl) => redirect(longUrl, StatusCodes.MovedPermanently)
          }
          case Failure(ex) => {
            logger.error(s"internal server error for GET $s", ex)
            complete(HttpResponse(StatusCodes.NotFound))
          }
        }
      }
    }

}

trait DicelyRoutesImpl extends DicelyRoutes with RedisClientImpl
