package org.jsun.dicely.model

/**
 * Created by jsun on 6/9/2017 AD.
 */
final case class ShortenResult(
                                short_url: String,
                                long_url: String,
                                hash: String,
                                new_hash: Boolean
)
