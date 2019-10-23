package rediz.net.message

import rediz.protocol._
import scodec.Decoder

final case class SimpleString(message: String) extends BackendMessage

object SimpleString {
  final val Tag = '+'
  val decoder: Decoder[SimpleString] =
    utf8crlf.as[SimpleString]
}
