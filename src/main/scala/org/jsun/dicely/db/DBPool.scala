package org.jsun.dicely.db

/**
 * Created by jsun on 6/11/2017 AD.
 */
trait DBPool {
  def getDBResource(): DBClient
}
