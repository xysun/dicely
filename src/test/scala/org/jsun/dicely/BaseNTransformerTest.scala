package org.jsun.dicely

import com.typesafe.config.ConfigFactory
import org.jsun.dicely.util.BaseNTransformer
import org.scalatest.FunSuite

import scala.util.Random

/**
 * Created by jsun on 6/9/2017 AD.
 */
class BaseNTransformerTest extends FunSuite
    with BaseNTransformer {

  test("should be bidirectional") {
    for (i <- 1 to 1000) {
      val seed = Random.nextInt(Integer.MAX_VALUE) // make sure it's positive
      assert(decode(encode(seed)) == seed)
    }
  }

  test("checksum should always produce 2 character") {
    val ALPHABET = ConfigFactory.load().getString("alphabet")
    val BASE = ALPHABET.length
    assert(toBaseN(BASE).length == 2)
    assert(toBaseN(BASE * BASE - 1).length == 2)
  }

}
