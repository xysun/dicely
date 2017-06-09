package org.jsun.dicely

import com.typesafe.config.ConfigFactory
import org.jsun.dicely.db.DBClient
import org.jsun.dicely.model.ShortenResponse
import org.jsun.dicely.util.{BaseNTransformer, ShortenResponseCreator, UrlEnricher, UrlHasher}


trait UrlShortener extends BaseNTransformer with UrlEnricher{

  this:UrlHasher with DBClient =>

  private val conf = ConfigFactory.load()

  def retrieve(shortUrl:String):Option[String] = dbGet(s"id:${decode(shortUrl)}")

  def shorten(longUrl:String):ShortenResponse = { // todo: try catch

    val base = s"${conf.getString("domain")}:${conf.getInt("port")}"

    // verify if it's a valid url first
    val enrichedUrlOption = enrichUrl(longUrl)
    if (enrichedUrlOption.isEmpty) return ShortenResponseCreator.INVALID_URL

    val enrichedUrl = enrichedUrlOption.get

    // todo: unit test on invalid url
    // todo: unit test: if stop and start, should generate same
    val key = s"hash:${hashUrl(enrichedUrl)}"

    dbGet(key) match {

      case None => {

        val id = dbIncr("id").get // todo: if id is None, can be handled with try catch

        val encoded = encode(id)

        // save to (hash(long_url), short_url) table
        dbSet(key, encoded)

        // save to (counter, long_url) table
        dbSet(s"id:$id", enrichedUrl)

        ShortenResponseCreator.createResponse(base, encoded, enrichedUrl, true)

      }

      case Some(s:String) => ShortenResponseCreator.createResponse(base, s, enrichedUrl, false)

    }

  }

}
