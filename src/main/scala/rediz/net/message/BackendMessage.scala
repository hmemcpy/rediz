package rediz.net.message

import scodec.Decoder

import scala.annotation.switch

trait BackendMessage

object BackendMessage {
  def decoder(tag: Byte): Decoder[BackendMessage] =
    (tag: @switch) match {
      case StringReply.Tag    => StringReply.decoder
      case BulkReply.Tag      => BulkReply.decoder
      case MultiBulkReply.Tag => MultiBulkReply.decoder
      case ErrorReply.Tag     => ErrorReply.decoder
      case _                  => UnknownReply.decoder(tag)
    }
}
