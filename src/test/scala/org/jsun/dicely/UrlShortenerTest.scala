package org.jsun.dicely

import com.redis.RedisClient
import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory

/**
  * Created by jsun on 6/9/2017 AD.
  */
class UrlShortenerTest extends FunSuite with MockFactory{

  val stubRedis = stub[RedisClient]

  // configure fakeDb behavior
  (fakeDb.getPlayerById _) when(222) returns(Player(222, "boris", Countries.Russia))
  (fakeDb.getPlayerById _) when(333) returns(Player(333, "hans", Countries.Germany))

}
