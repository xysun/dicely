package org.jsun.dicely.model

/**
  * Created by jsun on 6/6/2017 AD.
  *
  * routes:
  *
  * POST /api/v1/shorten
  * request: {"url":"xxx"}
  * response:
  * {
      "status_code": 200,
      "data": {
        "url": "http://bit.ly/2scqcO2",
        "long_url": "https://mvnrepository.com/artifact/org.json4s/json4s-jackson_2.11/3.5.2",
        "hash": "2scsNYh",
        "is_new_hash": 1
      },
      "status_txt": "OK"
    }
  *
  * GET /abcde -> 304 redirect
  *
  *
  */

final case class ShortenRequest(url:String)
