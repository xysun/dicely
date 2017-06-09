package org.jsun.dicely

import org.jsun.dicely.mock.DBClientMock
import org.scalatest.{ BeforeAndAfterEach, FunSuite }

class UrlShortenerTest extends FunSuite with BeforeAndAfterEach
    with UrlShortener
    with DBClientMock {

  override def beforeEach {
    resetDB()
  }

  test("valid url with protocol and www") {
    val u = "http://www.google.com"
    assert(enrichUrl(u).get == u)
  }

  test("no protocol still valid") {
    val u = "www.google.com"
    assert(enrichUrl(u).get == s"http://$u")
  }

  test("no protocol, no www, still valid") {
    val u = "google.com"
    assert(enrichUrl(u).get == s"http://$u")
  }

  test("white space shouldn't matter") {
    val u = "   google.com   "
    assert(enrichUrl(u).get == s"http://google.com")
  }

  test("invalid case") {
    val u = "abc"
    assert(enrichUrl(u).isEmpty)
  }

  test("previously hashed key should return new_hash = false") {

    val u = "http://test1.com"
    val hash = shorten(u).data.get.hash

    val result = shorten(u).data.get
    assert(!result.new_hash)
    assert(result.hash == hash)

    assert(retrieve(hash).get == u)

  }

  test("new hash should return new_hash = true and set") {
    val result = shorten("http://test1.com").data.get
    assert(result.new_hash)
    assert(counter == 1L)
  }
}
