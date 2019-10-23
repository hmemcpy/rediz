package rediz.net.message

import rediz.protocol._
import scodec.Codec
import scodec.codecs._

case class BulkReply(message: String) extends BackendMessage {
  override def toString: String = message
}

object BulkReply {
  final val Tag = '$'

  val decoder: Codec[BulkReply] =
    (variableSizeBytes(lengthCrlf, utf8) <~ constant(crlf))
      .as[BulkReply]
  
  val decoderNoTag: Codec[BulkReply] =
    constant(Tag) ~> decoder
}
