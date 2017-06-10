package org.jsun.dicely.util

import org.jsun.dicely.model.{ ShortenResponse, ShortenResult }

/**
 * Created by jsun on 6/8/2017 AD.
 */
object ResponseCreator {

  val INVALID_URL = ShortenResponse(
    status_code = 500,
    status_text = "INVALID_ARG_URL",
    data = None
  )

  val INTERNAL_SERVER_ERROR = ShortenResponse(
    status_code = 500,
    status_text = "INTERNAL_SERVER_ERROR",
    data = None
  )

  def create(hash: String, longUrl: String, isNewHash: Boolean) =
    ShortenResponse(
      status_code = 200,
      status_text = "OK",
      data = Some(ShortenResult(
        long_url = longUrl,
        hash = hash,
        new_hash = isNewHash
      ))
    )

}
