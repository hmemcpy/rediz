package rediz.net.message

import scodec.Encoder

trait RedisCommand[A] {
  def encoder: Encoder[A]
}

object RedisCommand {
  def apply[A](enc: Encoder[A]): RedisCommand[A] =
    new RedisCommand[A] {
      override def encoder: Encoder[A] = enc
    }
}
