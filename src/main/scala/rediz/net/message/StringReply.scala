package rediz.net.message

import rediz.protocol.Codecs._
import scodec.Decoder

final case class StringReply(message: String) extends BackendMessage {
  override def toString: String = message
}

object StringReply {
  final val Tag = '+'
  val decoder: Decoder[StringReply] =
    utf8crlf.as[StringReply]
}
