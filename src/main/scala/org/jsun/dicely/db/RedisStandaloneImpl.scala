package org.jsun.dicely.db

import com.typesafe.config.ConfigFactory
import redis.clients.jedis.Jedis

/**
 * Created by jsun on 6/9/2017 AD.
 */
trait RedisStandaloneImpl extends DBClient {

  private val conf = ConfigFactory.load()
  private val redisClient = new Jedis(conf.getString("redis.host"), conf.getInt("redis.port"))

  def dbIncr(key: String) = redisClient.incr(key)

  def dbGet(key: String) = Option(redisClient.get(key))

  def dbSet(key: String, value: String) = redisClient.set(key, value)

}
