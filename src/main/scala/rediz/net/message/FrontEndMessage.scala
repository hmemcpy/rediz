package rediz.net.message

import scodec.Encoder

trait FrontEndMessage[A] {
  def encoder: Encoder[A]
}

object FrontEndMessage {
  def apply[A](enc: Encoder[A]): FrontEndMessage[A] =
    new FrontEndMessage[A] {
      override def encoder: Encoder[A] = enc
    }
}
