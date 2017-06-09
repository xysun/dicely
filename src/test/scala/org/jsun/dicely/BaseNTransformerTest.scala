package org.jsun.dicely

import org.jsun.dicely.util.BaseNTransformer
import org.scalatest.FunSuite

import scala.util.Random

/**
 * Created by jsun on 6/9/2017 AD.
 */
class BaseNTransformerTest extends FunSuite
    with BaseNTransformer {

  test("should be bidirectional") {
    for (i <- 1 to 100) {
      val seed = Random.nextInt(Integer.MAX_VALUE) // make sure it's positive
      assert(decode(encode(seed)) == seed)
    }
  }

}
