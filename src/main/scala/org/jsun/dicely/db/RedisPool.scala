package org.jsun.dicely.db

import com.typesafe.config.ConfigFactory
import redis.clients.jedis.JedisPool

/**
 * Created by jsun on 6/11/2017 AD.
 */
trait RedisPool extends DBPool {
  private val conf = ConfigFactory.load()
  private val redisPool = {
    val host = conf.getString("redis.host")
    val port = conf.getInt("redis.port")
    new JedisPool(host, port)
  }

  override def getDBResource(): DBClient = new RedisClient(redisPool.getResource)
}
