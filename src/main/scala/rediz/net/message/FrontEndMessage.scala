package rediz.net.message

import scodec.Encoder
import scodec.codecs.utf8

trait FrontEndMessage[A] {
  def encoder: Encoder[A]
}

object FrontEndMessage {
  def apply[A](enc: Encoder[A]): FrontEndMessage[A] =
    new FrontEndMessage[A] {
      override def encoder: Encoder[A] = enc
    }


  def withCrlf[A](e: Encoder[A]): Encoder[A] =
    Encoder { a =>
      for {
        c    <- e.encode(a)
        crlf <- utf8.encode("\r\n")
      } yield c ++ crlf
    }

}
