package rediz.net.message

import scodec.Encoder

trait RedisMessage[A] {
  def encoder: Encoder[A]
}

object RedisMessage {
  def apply[A](enc: Encoder[A]): RedisMessage[A] =
    new RedisMessage[A] {
      override def encoder: Encoder[A] = enc
    }
}
