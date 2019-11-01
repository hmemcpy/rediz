package rediz.net.message

import scodec.Decoder
import scodec.bits.ByteVector
import scodec.codecs._

final case class UnknownResponse(tag: Byte, data: ByteVector) extends BackendMessage {
  override def toString: String = s"Unknown message(${tag.toChar}, $data)"
}

object UnknownResponse {
  def decoder(tag: Byte): Decoder[UnknownResponse] = bytes.map(bv => UnknownResponse(tag, bv))
}
