package rediz

import scodec.bits.{BitVector, ByteVector}
import scodec.{Attempt, Codec, DecodeResult, Encoder, Err, SizeBound}

package object protocol {
  val crlf: ByteVector = ByteVector('\r', '\n')

  implicit class EncOps[A](e: Encoder[A]) {
    def withCrlf: Encoder[A] =
      Encoder { a =>
        e.encode(a).map(_ ++ crlf.toBitVector)
      }
  }

  def lengthCrlf: Codec[Int] = new Codec[Int] {
    override def sizeBound: SizeBound                   = SizeBound.unknown
    override def encode(value: Int): Attempt[BitVector] = ???
    override def decode(bits: BitVector): Attempt[DecodeResult[Int]] =
      decodeUntilCrlf(bits) { b =>
        b.decodeAscii.map(_.toIntOption) match {
          case Left(err)    => Left(err.toString)
          case Right(value) => value.toRight("Unable to parse length integer")
        }
      }
  }

  def utf8crlf: Codec[String] = new Codec[String] {
    override def sizeBound: SizeBound                      = SizeBound.unknown
    override def encode(value: String): Attempt[BitVector] = ???
    override def decode(bits: BitVector): Attempt[DecodeResult[String]] = {
      decodeUntilCrlf(bits) { bv =>
        scodec.codecs.utf8
          .decodeValue(bv.toBitVector)
          .toEither
          .fold(err => Left(err.message), Right(_))
      }
    }
  }

  def decodeUntilCrlf[A](bits: BitVector)(
      decoder: ByteVector => Either[String, A]): Attempt[DecodeResult[A]] = {
    val bv  = bits.toByteVector
    val idx = bv.indexOfSlice(crlf)
    if (idx < 0 || idx > bv.length)
      Attempt.failure(Err("""Cannot find the terminating \r\n bytes"""))
    else {
      bv.consume(idx)(decoder) match {
        case Left(_) => Attempt.failure(Err.insufficientBits(idx, bv.size))
        case Right((remainder, l)) =>
          Attempt.successful(
            DecodeResult(l, remainder.drop(crlf.length).toBitVector)
          )
      }
    }
  }
}
