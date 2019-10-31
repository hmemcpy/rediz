package rediz.net.message

import rediz.protocol.Codecs._
import scodec.Codec
import scodec.codecs._

case class MultiBulkReply(replies: List[BulkReply]) extends BackendMessage {
  override def toString: String = replies.mkString("\n")
}

object MultiBulkReply {
  final val Tag = '*'

  val decoder: Codec[MultiBulkReply] =
    listOfN(lengthCrlf, BulkReply.decoderNoTag).as[MultiBulkReply]
}