package rediz.net.message

import rediz.protocol.Codecs._
import scodec.Codec
import scodec.codecs._

trait SimpleStringResponse extends BackendMessage

final case class SimpleString(message: String) extends SimpleStringResponse {
  override def toString: String = message
}

object SimpleStringResponse {
  final val Tag = '+'
  val decoder: Codec[SimpleString] =
    utf8crlf.as[SimpleString]

  val decoderNoTag: Codec[SimpleString] =
    constant(Tag) ~> decoder
}
