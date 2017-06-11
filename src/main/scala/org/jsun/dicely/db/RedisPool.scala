package org.jsun.dicely.db

import com.typesafe.config.ConfigFactory
import redis.clients.jedis.{ HostAndPort, JedisCluster, JedisPool }

/**
 * Created by jsun on 6/11/2017 AD.
 */
trait RedisPool extends DBPool {
  private val conf = ConfigFactory.load()

  private val host = conf.getString("redis.host")
  private val port = conf.getInt("redis.port")
  private val mode = conf.getString("redis.mode")

  private val redisPool: Option[JedisPool] = mode match {
    case "standalone" => Some(new JedisPool(host, port))
    case _ => None
  }

  private val redisCluster: Option[JedisCluster] = mode match {
    case "cluster" => Some(new JedisCluster(new HostAndPort(host, port)))
    case _ => None
  }

  override def getDBResource(): DBClient = mode match {
    case "standalone" => {
      require(redisPool.nonEmpty)
      new RedisClient(redisPool.get.getResource)
    }
    case "cluster" => {
      require(redisCluster.nonEmpty)
      new RedisCluster(redisCluster.get)
    }
  }
}
