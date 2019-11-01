package rediz.net.message

import rediz.protocol.Codecs._
import scodec.codecs._
import scodec.{Codec, Decoder}

sealed trait BulkResponse extends BackendMessage

final case class BulkString(message: String) extends BulkResponse {
  override def toString: String = message
}

object BulkString {
  def decoder(size: Int): Decoder[BulkString] =
    (variableSizeBytes(provide(size), utf8) <~ constant(crlf)).map(BulkString(_))
}

case object NullBulkString extends BulkResponse {
  override def toString: String = "(nil)"
  val decoder: Decoder[NullBulkString.type] = Decoder.point(NullBulkString)
}

object BulkResponse {
  final val Tag = '$'

  val decoder: Codec[BulkResponse] =
    Codec(
      emptyEncoder,
      lengthCrlf.flatMap {
        case -1   => NullBulkString.decoder
        case size => BulkString.decoder(size)
      },
    )

  val decoderNoTag: Codec[BulkResponse] =
    constant(Tag) ~> decoder
}
