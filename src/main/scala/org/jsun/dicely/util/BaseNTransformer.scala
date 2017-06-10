package org.jsun.dicely.util

/**
 * Created by jsun on 6/8/2017 AD.
 */
trait BaseNTransformer {

  // todo: scramble alphabet, some kind of randomization for prefix (id mod?)

  private val ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_"
  private val BASE = ALPHABET.length

  // todo: how to prevent ppl enumerate all integers
  /* it might be useful to add a two character checksum to the url. That would prevent direct iteration of all the urls in your system. Something simple like f(checksum(id) % (62^2)) + f(id) = url_id”

   */

  def decode(str: String): Long = {
    str.foldLeft(0L)((num, c) => num * BASE + ALPHABET.indexOf(c))
  }

  def encode(i: Long): String = {
    val str: StringBuilder = new StringBuilder

    var num = i // todo: more scala
    while (num > 0) {
      str.insert(0, ALPHABET.charAt((num % BASE).toInt))
      num /= BASE
    }

    str.mkString
  }

}
