package org.jsun.dicely.db

import com.redis.RedisClient
import com.typesafe.config.ConfigFactory

/**
 * Created by jsun on 6/9/2017 AD.
 */
trait RedisClientImpl extends DBClient {

  private val conf = ConfigFactory.load()
  private val redisClient = new RedisClient(conf.getString("redis.host"), conf.getInt("redis.port"))

  def dbIncr(key: String) = redisClient.incr(key)

  def dbGet(key: String) = redisClient.get(key)

  def dbSet(key: String, value: String) = {
    redisClient.set(key, value)
    Unit
  }

}
