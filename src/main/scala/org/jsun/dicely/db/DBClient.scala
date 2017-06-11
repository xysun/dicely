package org.jsun.dicely.db

/**
 * Created by jsun on 6/9/2017 AD.
 */
trait DBClient {

  // for url shortener, we need: 1. atomic inr 2. get 3. set
  def incr(key: String): Long

  def get(key: String): Option[String]

  def set(key: String, value: String): Unit

  def close(): Unit

}

