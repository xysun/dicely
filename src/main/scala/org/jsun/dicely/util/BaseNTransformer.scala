package org.jsun.dicely.util

import com.typesafe.config.ConfigFactory

import scala.util.Random

/**
 * Created by jsun on 6/8/2017 AD.
 */
trait BaseNTransformer {

  private val ALPHABET = ConfigFactory.load().getString("alphabet")
  private val BASE = ALPHABET.length

  def toBaseN(i: Long): String = {
    val str: StringBuilder = new StringBuilder

    var num = i
    while (num > 0) {
      str.insert(0, ALPHABET.charAt((num % BASE).toInt))
      num /= BASE
    }

    str.mkString
  }

  def decode(str: String): Long = {
    // first two char is checksum
    require(str.length >= 3)
    str.substring(2).foldLeft(0L)((num, c) => num * BASE + ALPHABET.indexOf(c))
  }

  def encode(i:Long):String = {

    // with two character checksum to prevent direct iteration of all urls
    // f(checksum(id) % (62^2)) + f(id) = url_id

    val part2 = toBaseN(i)
    val checksum = Random.nextInt(BASE * (BASE - 1)) + BASE // [BASE, BASE * BASE)
    val part1 = toBaseN(checksum)
    require(part1.length == 2)

    s"$part1$part2"
  }

}
