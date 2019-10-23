package rediz.net.message

import scodec.Decoder
import scodec.bits.ByteVector
import scodec.codecs._

final case class UnknownReply(tag: Byte, data: ByteVector) extends BackendMessage {
  override def toString: String = s"Unknown message(${tag.toChar}, $data)"
}

object UnknownReply {
  def decoder(tag: Byte): Decoder[UnknownReply] = bytes.map(bv => UnknownReply(tag, bv))
}
