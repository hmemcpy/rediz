package rediz.net.message

import rediz.protocol._
import scodec.Decoder
import scodec.codecs._

case class BulkString(strings: List[String]) extends BackendMessage

object BulkString {
  final val Tag = '$'

  def strings(size: Int): Decoder[BulkString] =
    bytes(size).map(_.decodeUtf8.toOption).map {
      case None    => BulkString(Nil)
      case Some(s) => BulkString(List(s))
    }

  val decoder: Decoder[BulkString] =
    lengthCrlf.flatMap(strings).as[BulkString]
}
