package rediz.net.message

import scodec.Decoder

import scala.annotation.switch

trait BackendMessage

object BackendMessage {
  def decoder(tag: Byte): Decoder[BackendMessage] =
    (tag: @switch) match {
      case SimpleString.Tag => SimpleString.decoder
      case BulkString.Tag   => BulkString.decoder
      case _                => UnknownMessage.decoder(tag)
    }
}
