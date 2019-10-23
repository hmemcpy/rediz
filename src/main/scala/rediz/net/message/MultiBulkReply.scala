package rediz.net.message

import rediz.protocol._
import scodec.Codec
import scodec.codecs._

case class MultiBulkReply(replies: List[BulkReply]) extends BackendMessage

object MultiBulkReply {
  final val Tag = '*'

  val decoder: Codec[MultiBulkReply] =
    listOfN(lengthCrlf, BulkReply.decoderNoTag).as[MultiBulkReply]
}