package org.jsun.dicely.util

import java.nio.ByteBuffer
import java.util.UUID

import com.google.common.base.Charsets
import com.google.common.hash.Hashing

/**
  * Created by jsun on 6/8/2017 AD.
  */
trait UrlHasher{ // easier to mock for unit test
  def hashUrl(url:String):String
}

trait UrlHasherImpl extends UrlHasher{
  def hashUrl(url:String):String = {
    // urls are ascii; with 128 bits we have 2**64 = million billion chance of collission
    val bytes = Hashing.murmur3_128().hashString(url, Charsets.US_ASCII).asBytes()
    val byteBuffer = ByteBuffer.wrap(bytes)
    new UUID(byteBuffer.getLong, byteBuffer.getLong).toString
  }
}
