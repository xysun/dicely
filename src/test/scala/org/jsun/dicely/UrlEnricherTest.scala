package org.jsun.dicely

import org.jsun.dicely.util.UrlEnricher
import org.scalatest.FunSuite

/**
  * Created by jsun on 6/9/2017 AD.
  */
class UrlEnricherTest extends FunSuite with UrlEnricher{

  test("valid url with protocol and www"){
    val u = "http://www.google.com"
    assert(enrichUrl(u).get == u)
  }

  test("no protocol still valid"){
    val u = "www.google.com"
    assert(enrichUrl(u).get == s"http://$u")
  }

  test("no protocol, no www, still valid"){
    val u = "google.com"
    assert(enrichUrl(u).get == s"http://$u")
  }

  test("white space shouldn't matter"){
    val u = "   google.com   "
    assert(enrichUrl(u).get == s"http://google.com")
  }

  test("invalid case"){
    val u = "abc"
    assert(enrichUrl(u).isEmpty)
  }

}
