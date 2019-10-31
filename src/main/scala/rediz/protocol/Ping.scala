package rediz.protocol

import rediz.net.message.RedisCommand
import scodec.Encoder

case class Ping(message: String = "")

object Ping {
  implicit val PingMessage = RedisCommand {
    Encoder { p: Ping =>
      p.message match {
        case m if m.isEmpty => encode("PING")
        case m              => encode("PING", m)
      }
    }
  }
}
