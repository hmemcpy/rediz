package rediz.net.message

import scodec.Decoder

import scala.annotation.switch

trait BackendMessage

object BackendMessage {
  def decoder(tag: Byte): Decoder[BackendMessage] =
    (tag: @switch) match {
      case SimpleStringResponse.Tag => SimpleStringResponse.decoder
      case BulkResponse.Tag         => BulkResponse.decoder
      case MultiBulkResponse.Tag    => MultiBulkResponse.decoder
      case ErrorResponse.Tag        => ErrorResponse.decoder
      case IntegerResponse.Tag      => IntegerResponse.decoder
      case _                        => UnknownResponse.decoder(tag)
    }
}
