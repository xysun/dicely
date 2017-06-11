package org.jsun.dicely.db

import redis.clients.jedis.{ Jedis, JedisCluster }

/**
 * Created by jsun on 6/11/2017 AD.
 */
class RedisClient(r: Jedis) extends DBClient {
  // wrapper

  def incr(key: String): Long = r.incr(key)

  def get(key: String): Option[String] = Option(r.get(key))

  def set(key: String, value: String): Unit = r.set(key, value)

  def close(): Unit = r.close()
}

class RedisCluster(r: JedisCluster) extends DBClient {
  def incr(key: String): Long = r.incr(key)

  def get(key: String): Option[String] = Option(r.get(key))

  def set(key: String, value: String): Unit = r.set(key, value)

  def close(): Unit = ()
}
