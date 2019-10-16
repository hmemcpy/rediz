package rediz.protocol

import scodec.bits.BitVector

case class Unknown (tag: Char, data: BitVector)
