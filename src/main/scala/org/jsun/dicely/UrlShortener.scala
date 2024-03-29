package org.jsun.dicely

import java.nio.ByteBuffer
import java.util.UUID

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import com.netaporter.uri.Uri.parse
import com.typesafe.config.ConfigFactory
import org.apache.commons.validator.routines.UrlValidator
import org.jsun.dicely.db.{ Closeable, DBClient, DBPool }
import org.jsun.dicely.model.ShortenResponse
import org.jsun.dicely.util._

trait UrlShortener extends BaseNTransformer with Closeable {

  this: DBPool =>

  private val conf = ConfigFactory.load()

  private val urlValidator = new UrlValidator()

  def hashUrl(url: String): String = {
    // urls are ascii; with 128 bits we have 2**64 = million billion chance of collission
    val bytes = Hashing.murmur3_128().hashString(url, Charsets.US_ASCII).asBytes()
    val byteBuffer = ByteBuffer.wrap(bytes)
    new UUID(byteBuffer.getLong, byteBuffer.getLong).toString
  }

  def enrichUrl(url: String): Option[String] = {
    // validate and add protocol if missing; default http
    val strippedUrl = url.replace(" ", "")
    val s = parse(strippedUrl)
    val enrichedUrl = if (s.protocol.isEmpty) s"http://$strippedUrl" else strippedUrl
    if (urlValidator.isValid(enrichedUrl)) Some(enrichedUrl) else None
  }

  def retrieve(shortUrl: String): Option[String] = {

    if (shortUrl.length < 3) return None // fast fail

    using(getDBResource()) { r =>
      r.get(s"id:${decode(shortUrl)}")
    }
  }

  def shorten(longUrl: String): ShortenResponse = {

    val base = {
      if (conf.getInt("get-port") == 80) s"${conf.getString("domain")}"
      else s"${conf.getString("domain")}:${conf.getInt("get-port")}"
    }

    // verify if it's a valid url first
    val enrichedUrlOption = enrichUrl(longUrl)
    if (enrichedUrlOption.isEmpty) return ResponseCreator.INVALID_URL

    val enrichedUrl = enrichedUrlOption.get

    val key = s"hash:${hashUrl(enrichedUrl)}"

    using(getDBResource()) {
      r =>
        {
          r.get(key) match {

            case None => {

              val id = r.incr("id")

              val encoded = encode(id)

              // save to (hash(long_url), short_url) table
              r.set(key, encoded)

              // save to (counter, long_url) table
              r.set(s"id:$id", enrichedUrl)

              ResponseCreator.create(base, encoded, enrichedUrl, true)

            }

            case Some(s: String) => ResponseCreator.create(base, s, enrichedUrl, false)
          }
        }
    }
  }

}
