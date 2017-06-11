package org.jsun.dicely.mock

import org.jsun.dicely.db.{ DBClient, DBPool }

/**
 * Created by jsun on 6/9/2017 AD.
 */

object DBClientMock extends DBClient {

  var counter = 0L
  var setMap: Map[String, String] = Map.empty

  def resetDB() = {
    counter = 0L
    setMap = Map.empty
  }

  def get(key: String) = setMap.get(key)

  def incr(key: String) = {
    counter += 1
    counter
  }

  def set(key: String, value: String) = {
    setMap += (key -> value)
  }

  def close() = ()
}

trait DBMockPool extends DBPool {
  def getDBResource() = DBClientMock
}

object DBClientExceptionMock extends DBClient {

  def get(key: String) = {
    throw new Exception("mock exception in dbGet!")
  }

  def close() = ()

  def incr(key: String) = {
    throw new Exception("mock exception in dbIncr!")
  }

  def set(key: String, value: String) = {
    throw new Exception("mock exception in dbSet!")
  }

}

trait DBExceptionMockPool extends DBPool {
  def getDBResource() = DBClientExceptionMock
}
