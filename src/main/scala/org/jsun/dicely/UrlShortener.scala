package org.jsun.dicely

import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import org.jsun.dicely.model.{ShortenRequest, ShortenResponse}
import org.jsun.dicely.util.{BaseNTransformer, ShortenResponseCreator, UrlEnricher, UrlHasher}


class UrlShortener(redisClient:RedisClient) extends BaseNTransformer with UrlEnricher{

  this:UrlHasher =>

  private val conf = ConfigFactory.load()

  def retrieve(shortUrl:String):Option[String] = redisClient.get(s"id:${decode(shortUrl)}")

  def shorten(req:ShortenRequest):ShortenResponse = { // todo: try catch

    val base = s"${conf.getString("domain")}:${conf.getInt("port")}"

    // verify if it's a valid url first
    val enrichedUrlOption = enrichUrl(req.url)
    if (enrichedUrlOption.isEmpty) return ShortenResponseCreator.INVALID_URL

    val enrichedUrl = enrichedUrlOption.get

    // todo: unit test on invalid url
    // todo: unit test: if stop and start, should generate same
    val key = s"hash:${hashUrl(enrichedUrl)}"

    redisClient.get(key) match {

      case None => {

        val id = redisClient.incr("id").get // todo: if id is None, can be handled with try catch

        val encoded = encode(id)

        // save to (hash(long_url), short_url) table
        redisClient.set(key, encoded)

        // save to (counter, long_url) table
        redisClient.set(s"id:$id", enrichedUrl)

        ShortenResponseCreator.createResponse(base, encoded, enrichedUrl, true)

      }

      case Some(s:String) => ShortenResponseCreator.createResponse(base, s, enrichedUrl, false)

    }

  }

}
