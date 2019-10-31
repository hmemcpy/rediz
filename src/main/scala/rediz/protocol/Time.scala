package rediz.protocol

import rediz.net.message.RedisCommand
import scodec.Encoder
import scodec.codecs._

case class Time()

object Time {
  implicit val TimeMessage = RedisCommand {
    Encoder { _: Time =>
      encode("TIME")
    }
  }
}
