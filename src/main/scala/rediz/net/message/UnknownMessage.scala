package rediz.net.message

import scodec.Decoder
import scodec.bits.ByteVector
import scodec.codecs._

final case class UnknownMessage(tag: Byte, data: ByteVector) extends BackendMessage {
  override def toString: String = s"Unknown message(${tag.toChar}, $data)"
}

object UnknownMessage {
  def decoder(tag: Byte): Decoder[UnknownMessage] = bytes.map(bv => UnknownMessage(tag, bv))
}
