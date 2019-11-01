package rediz.protocol

import rediz.protocol.Codecs._
import scodec.Encoder

case class RedisCommand(cmd: String, args: String*)

object RedisCommand {
  implicit val redisMessage = RedisMessage {
    Encoder { c: RedisCommand =>
      encode(c.cmd, c.args:_*)
    }
  }
}
