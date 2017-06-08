package org.jsun

import java.nio.ByteBuffer
import java.util.UUID

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import com.redis.RedisClient
import org.jsun.routes.{ShortenRequest, ShortenResponse, ShortenResult}

/**
  * Created by jsun on 6/8/2017 AD.
  */
class UrlShortener extends Encoder{

  private val redisClient = new RedisClient("localhost", 6379) // todo: mock for unit test

  def retrieve(shortUrl:String):Option[String] = {

    val id = decode(shortUrl)
    redisClient.get(s"id:$id")
  }

  def shorten(req:ShortenRequest):ShortenResponse = {

    // todo: verify it's a valid url first; abc.com; www.abc.com; http://abc.com; http://www.abc.com

    // urls are ascii; with 128 bits we have 2**64 = million billion chance of collission
    val bytes = Hashing.murmur3_128().hashString(req.url, Charsets.US_ASCII).asBytes()
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
        redisClient.set(s"id:$id", req.url)

        ShortenResponse(
          status_code = 200,
          status_text = "OK",
          data = ShortenResult(
            url = s"http://dice.ly/$encoded",
            long_url = req.url,
            hash = encoded,
            new_hash = true
          )
        )

      }
      case Some(s:String) => {
        ShortenResponse(
          status_code = 200,
          status_text = "OK",
          data = ShortenResult(
            url = s"http://dice.ly/$s",
            long_url = req.url,
            hash = s,
            new_hash = false
          )
        )
      }
    }

  }

}
