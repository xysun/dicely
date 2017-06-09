package org.jsun.dicely

import java.nio.ByteBuffer
import java.util.UUID

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import com.netaporter.uri.Uri.parse
import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import org.apache.commons.validator.routines.UrlValidator
import org.jsun.dicely.model.{ShortenRequest, ShortenResponse}
import org.jsun.dicely.util.{BaseNTransformer, ShortenResponseCreator, UrlEnricher}

/**
  * Created by jsun on 6/8/2017 AD.
  */
class UrlShortener(redisClient:RedisClient) extends BaseNTransformer with UrlEnricher{

  private val conf = ConfigFactory.load()

  def retrieve(shortUrl:String):Option[String] = redisClient.get(s"id:${decode(shortUrl)}")

  def shorten(req:ShortenRequest):ShortenResponse = { // todo: try catch

    val base = s"${conf.getString("domain")}:${conf.getInt("port")}"

    // verify if it's a valid url first
    val enrichedUrlOption = enrichUrl(req.url)
    if (enrichedUrlOption.isEmpty) return ShortenResponseCreator.INVALID_URL

    val enrichedUrl = enrichedUrlOption.get

    // todo: unit test on invalid url

    // urls are ascii; with 128 bits we have 2**64 = million billion chance of collission
    val bytes = Hashing.murmur3_128().hashString(enrichedUrl, Charsets.US_ASCII).asBytes()
    val byteBuffer = ByteBuffer.wrap(bytes)
    // todo: unit test: if stop and start, should generate same
    val key = s"hash:${new UUID(byteBuffer.getLong, byteBuffer.getLong)}"

    redisClient.get(key) match {

      case None => {

        val id = redisClient.incr("id").get // todo: if id is None

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
