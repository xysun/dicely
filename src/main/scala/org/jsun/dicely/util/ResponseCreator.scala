package org.jsun.dicely.util

import org.jsun.dicely.model.{ShortenResponse, ShortenResult}

/**
  * Created by jsun on 6/8/2017 AD.
  */
object ResponseCreator {

  val INVALID_URL = ShortenResponse(
    status_code = 500,
    status_text = "INVALID_ARG_URL",
    data = None
  )

  def create(domain:String, hash:String, longUrl:String, isNewHash:Boolean) =
    ShortenResponse(
      status_code = 200,
      status_text = "OK",
      data = Some(ShortenResult(
        url = s"http://$domain/$hash",
        long_url = longUrl,
        hash = hash,
        new_hash = isNewHash
      ))
  )

}
