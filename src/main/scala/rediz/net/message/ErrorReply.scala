package rediz.net.message

import rediz.protocol.Codecs._
import scodec.Codec
import scodec.codecs._

case class ErrorReply(prefix: String, message: String) extends BackendMessage {
  override def toString: String = s"-$prefix $message"
}

object ErrorReply {
  final val Tag = '-'

  val decoder: Codec[ErrorReply] = {
    ( "prefix"  | utf8space ) ::
    ( "message" | utf8crlf  )
  }.as[ErrorReply]
}
