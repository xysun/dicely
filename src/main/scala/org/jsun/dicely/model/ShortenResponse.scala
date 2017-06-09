package org.jsun.dicely.model

/**
 * Created by jsun on 6/9/2017 AD.
 */
final case class ShortenResponse(
  status_code: Int,
  status_text: String,
  data: Option[ShortenResult]
)
