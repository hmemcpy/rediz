package rediz.protocol

import rediz.net.message.FrontEndMessage
import scodec.Encoder
import scodec.codecs._

case class Ping(message: String = "")

object Ping {
  implicit val PingMessage = FrontEndMessage {
    val payload: Encoder[Ping] =
      Encoder { p =>
        p.message match {
          case m if m.isEmpty => utf8.encode("PING")
          case m              => utf8.encode(s"PING '$m'")
        }
      }
    payload.withCrlf
  }
}
