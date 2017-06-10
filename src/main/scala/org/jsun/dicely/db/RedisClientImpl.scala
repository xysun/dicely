package org.jsun.dicely.db

import com.typesafe.config.ConfigFactory
import redis.clients.jedis.{HostAndPort, Jedis, JedisCluster}

/**
 * Created by jsun on 6/9/2017 AD.
 */
trait RedisClientImpl extends DBClient {

  // support both standalone and cluster mode

  private val conf = ConfigFactory.load()

  private val redisClient = {
    val host = conf.getString("redis.host")
    val port = conf.getInt("redis.port")
    if (conf.getString("redis.mode") == "standalone") new Jedis(host, port)
    else new JedisCluster(new HostAndPort(host, port)) // jedis will auto discover other nodes
  }

  def dbIncr(key: String) = redisClient.incr(key)

  def dbGet(key: String) = Option(redisClient.get(key))

  def dbSet(key: String, value: String) = redisClient.set(key, value)

}
