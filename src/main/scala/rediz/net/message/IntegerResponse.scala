package rediz.net.message

import rediz.protocol.Codecs._
import scodec.Codec
import scodec.codecs.constant

trait IntegerResponse extends BackendMessage

final case class Integer(value: Int) extends IntegerResponse {
  override def toString: String = s"(integer) $value"
}

object IntegerResponse {
  final val Tag = ':'

  val decoder: Codec[Integer] =
    lengthCrlf.as[Integer]

  val decoderNoTag: Codec[Integer] =
    constant(Tag) ~> decoder
}
