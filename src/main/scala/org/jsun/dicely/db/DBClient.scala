package org.jsun.dicely.db

import com.redis.RedisClient
import com.typesafe.config.ConfigFactory

/**
 * Created by jsun on 6/9/2017 AD.
 */
trait DBClient {

  // for url shortener, we need: 1. atomic inr 2. get 3. set
  def dbIncr(key: String): Option[Long]

  def dbGet(key: String): Option[String]

  def dbSet(key: String, value: String): Unit

}

