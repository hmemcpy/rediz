package rediz.protocol

import scodec._
import scodec.bits.{BitVector, ByteVector}
import scodec.codecs._

object Codecs {
  val crlf: ByteVector = ByteVector('\r', '\n')
  val space: ByteVector = ByteVector(0x20)

  def encode[A](cmd: String, args: String*): Attempt[BitVector] = {
    def encodeArg: Encoder[String] =
      Encoder { s =>
        for {
          b   <- byte.encode('$')
          len <- lengthCrlf.encode(s.length)
          cmd <- utf8until(crlf).encode(s)
        } yield b ++ len ++ cmd
      }

    for {
      argsByte <- byte.encode('*')
      argsLen  <- lengthCrlf.encode(args.length + 1)
      cmd      <- encodeArg.encode(cmd)
      args     <- Encoder.encodeSeq(encodeArg)(args)
    } yield argsByte ++ argsLen ++ cmd ++ args
  }

  val lengthCrlf: Codec[Int] = new Codec[Int] {
    override def sizeBound: SizeBound = SizeBound.unknown
    override def encode(value: Int): Attempt[BitVector] =
      utf8.encode(value.toString).map(_ ++ crlf.toBitVector)
    override def decode(bits: BitVector): Attempt[DecodeResult[Int]] =
      decodeUntil(bits)(crlf) { b =>
        b.decodeAscii.map(_.toIntOption) match {
          case Left(err)    => Left(err.toString)
          case Right(value) => value.toRight("Unable to parse length integer")
        }
      }
  }

  val utf8space = utf8until(space)

  val utf8crlf = utf8until(crlf)

  def utf8until(until: ByteVector): Codec[String] = new Codec[String] {
    override def sizeBound: SizeBound = SizeBound.unknown
    override def encode(value: String): Attempt[BitVector] =
      utf8.encode(value).map(_ ++ until.toBitVector)
    override def decode(bits: BitVector): Attempt[DecodeResult[String]] = {
      decodeUntil(bits)(until) { bv =>
        scodec.codecs.utf8
          .decodeValue(bv.toBitVector)
          .toEither
          .fold(err => Left(err.message), Right(_))
      }
    }
  }

  private def decodeUntil[A](bits: BitVector)(until: ByteVector)(
      decoder: ByteVector => Either[String, A]): Attempt[DecodeResult[A]] = {
    val bv  = bits.toByteVector
    val idx = bv.indexOfSlice(until)
    if (idx < 0 || idx > bv.length)
      Attempt.failure(Err(s"""Cannot find the $until bytes"""))
    else {
      bv.consume(idx)(decoder) match {
        case Left(_) => Attempt.failure(Err.insufficientBits(idx, bv.size))
        case Right((remainder, l)) =>
          Attempt.successful(
            DecodeResult(l, remainder.drop(until.length).toBitVector)
          )
      }
    }
  }
}
