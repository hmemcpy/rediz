package rediz.protocol

import rediz.net.message.FrontEndMessage
import scodec.Encoder
import scodec.codecs._

case class Time()

object Time {
  implicit val TimeMessage = FrontEndMessage {
    val payload: Encoder[Time] =
      Encoder { _ =>
        utf8.encode("TIME")
      }

    payload.withCrlf
  }
}
