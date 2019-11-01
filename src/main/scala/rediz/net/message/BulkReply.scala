package rediz.net.message

import rediz.protocol.Codecs._
import scodec.codecs._
import scodec.{Codec, Decoder}

sealed trait BulkReply extends BackendMessage with Product

final case class BulkStringReply(message: String) extends BulkReply {
  override def toString: String = message
}

object BulkStringReply {
  def decoder(size: Int): Decoder[BulkStringReply] =
    (variableSizeBytes(provide(size), utf8) <~ constant(crlf)).map(BulkStringReply(_))
}

case object NullReply extends BulkReply {
  override def toString: String = "(nil)"
  val decoder: Decoder[NullReply.type] = Decoder.point(NullReply)
}

object BulkReply {
  final val Tag = '$'

  val decoder: Codec[BulkReply] =
    Codec(
      emptyEncoder,
      lengthCrlf.flatMap {
        case -1   => NullReply.decoder
        case size => BulkStringReply.decoder(size)
      },
    )

  val decoderNoTag: Codec[BulkReply] =
    constant(Tag) ~> decoder
}
