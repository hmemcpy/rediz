package rediz.protocol

import rediz.net.message.FrontEndMessage
import rediz.net.message.FrontEndMessage._
import scodec.Encoder
import scodec.codecs._

case class Ping(message: Option[String] = None)

object Ping {
  implicit val PingMessage = FrontEndMessage {
    val payload: Encoder[Ping] =
      Encoder { p =>
        p.message match {
          case None          => utf8.encode("PING")
          case Some(message) => utf8.encode(s"PING '$message'")
        }
      }
    payload.withCrlf
  }
}
