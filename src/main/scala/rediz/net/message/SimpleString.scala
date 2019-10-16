package rediz.net.message

import scodec.Decoder
import scodec.codecs._

final case class SimpleString(message: String) extends BackendMessage

object SimpleString {
  final val Tag = '+'
  val decoder: Decoder[BackendMessage] =
    utf8.map(str => SimpleString(str.trim))
}
