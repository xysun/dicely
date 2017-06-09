package org.jsun.dicely.mock

import org.jsun.dicely.db.DBClient

/**
  * Created by jsun on 6/9/2017 AD.
  */
trait DBClientMock extends DBClient{

  var counter = 0L
  var setMap:Map[String,String] = Map.empty

  def resetDB() = {
    counter = 0L
    setMap = Map.empty
  }

  def dbGet(key:String) = setMap.get(key)

  def dbIncr(key:String) = {
    counter += 1
    Some(counter)
  }

  def dbSet(key:String, value:String) = {
    setMap += (key -> value)
  }
}
