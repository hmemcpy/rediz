package rediz.protocol

import rediz.net.message.FrontEndMessage
import rediz.net.message.FrontEndMessage._
import scodec.Encoder
import scodec.codecs._

case class Ping()

object Ping {
  implicit val PingMessage = FrontEndMessage {
    val payload: Encoder[Ping] =
      Encoder { _ =>
        utf8.encode("PING")
      }

    withCrlf(payload)
  }
}
