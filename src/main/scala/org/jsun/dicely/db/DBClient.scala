package org.jsun.dicely.db

import com.typesafe.config.ConfigFactory
import redis.clients.jedis.{ Jedis, JedisPool }

/**
 * Created by jsun on 6/9/2017 AD.
 */
trait DBClient {

  // for url shortener, we need: 1. atomic inr 2. get 3. set
  def incr(key: String): Long

  def get(key: String): Option[String]

  def set(key: String, value: String): Unit

  def close(): Unit

}

