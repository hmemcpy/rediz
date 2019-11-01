package rediz.net.message

import rediz.protocol.Codecs._
import scodec.Codec
import scodec.codecs._

trait ErrorResponse extends BackendMessage

case class Error(prefix: String, message: String) extends ErrorResponse {
  override def toString: String = s"-$prefix $message"
}

object ErrorResponse {
  final val Tag = '-'

  val decoder: Codec[Error] = {
    ( "prefix"  | utf8space ) ::
    ( "message" | utf8crlf  )
  }.as[Error]

  val decoderNoTag: Codec[Error] =
    constant(Tag) ~> decoder
}
