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
class UrlShortener {

  private val redisClient = new RedisClient("localhost", 6379) // todo: mock for unit test

  def shorten(req:ShortenRequest):ShortenResponse = {

    // urls are ascii; with 128 bits we have 2**64 = million billion chance of collission
    val bytes = Hashing.murmur3_128().hashString(req.url, Charsets.US_ASCII).asBytes()
    val byteBuffer = ByteBuffer.wrap(bytes)
    // todo: unit test: if stop and start, should generate same
    val key = s"hash:${new UUID(byteBuffer.getLong, byteBuffer.getLong)}"

    redisClient.get(key) match {

      case None => {

        val id = redisClient.incr("id") // todo: if id is None



        // save to big hash table
        redisClient.set(key, id.get.toString)

        ShortenResponse(
          status_code = 200,
          status_text = "OK",
          data = ShortenResult(
            url = s"http://dice.ly/${id.get}",
            long_url = req.url,
            hash = id.get.toString,
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
