package org.jsun.dicely

import org.jsun.dicely.db.DBClient
import org.jsun.dicely.util.UrlHasher
import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by jsun on 6/9/2017 AD.
  */

trait UrlHasherMock extends UrlHasher{
  def hashUrl(url: String): String = url match {
    case "http://test1.com" => "abc"
    case "http://test2.com" => "def"
  }

}

trait DBClientMock extends DBClient{

  var counter = 0L
  var setMap:Map[String,String] = Map.empty

  def resetDB() = {
    counter = 0L
    setMap = Map.empty
  }

  def dbGet(key:String) = key match {
    case "hash:abc" => Some("xkcd")
    case _ => None
  }

  def dbIncr(key:String) = {
    counter += 1
    Some(counter)
  }

  def dbSet(key:String, value:String) = {
    setMap += (key -> value)
  }
}

class UrlShortenerTest extends FunSuite with BeforeAndAfterEach
with UrlShortener
with UrlHasherMock
with DBClientMock {

  override def beforeEach{
    resetDB()
  }

  test("previously hashed key hash:abc should return new_hash = false"){

    val shortUrl = "xkcd"
    val result = shorten("http://test1.com").data.get
    assert(!result.new_hash)
    assert(result.hash == shortUrl)

  }


  test("new hash should return new_hash = true and set"){
    val result = shorten("http://test2.com").data.get
    assert(result.new_hash)
    assert(counter == 1L)
    assert(setMap.contains("hash:def"))
  }
}
