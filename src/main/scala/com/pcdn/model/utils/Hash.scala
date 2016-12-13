package com.pcdn.model.utils

import scala.util.hashing.MurmurHash3

/**
  * Created by Hung on 9/14/16.
  */
trait Hash {
  def toLong(x: String) = MurmurHash3.stringHash(x).toLong

  def toHexString(x: String) = MurmurHash3.stringHash(x).toHexString
}
