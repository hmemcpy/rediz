package rediz

import rediz.net.BitVectorSocket
import scodec.codecs._
import zio.Task

package object protocol {

  def readUnknown(
      bvs: BitVectorSocket
  ): Task[Unknown] =
    bvs.read(1).flatMap { bits =>
      val tag = byte.decodeValue(bits).require
      bvs.read(???).map(Unknown(tag.toChar, _))
    }
}
