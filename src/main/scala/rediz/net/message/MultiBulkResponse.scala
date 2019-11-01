package rediz.net.message

import rediz.protocol.Codecs._
import scodec.codecs._
import scodec.{Codec, Decoder}

sealed trait MultiBulkResponse extends BackendMessage

case class Array(replies: List[BackendMessage]) extends MultiBulkResponse {
  override def toString: String = replies match {
    case Nil => "(empty list or set)"
    case _   => replies.zipWithIndex.map { case (m, i) => s"${i + 1}) $m" }.mkString("\n")
  }
}

object Array {
  def decoder(size: Int): Codec[Array] =
    listOfN(
      provide(size),
      choice(
        SimpleStringResponse.decoderNoTag.upcast[BackendMessage],
        BulkResponse.decoderNoTag.upcast[BackendMessage],
        IntegerResponse.decoderNoTag.upcast[BackendMessage]
      )
    ).as[Array]
}

case object NullArray extends MultiBulkResponse {
  override def toString: String        = "(nil)"
  val decoder: Decoder[NullArray.type] = Decoder.point(NullArray)
}

object MultiBulkResponse {
  final val Tag = '*'

  val decoder: Codec[MultiBulkResponse] =
    Codec(
      emptyEncoder,
      lengthCrlf.flatMap {
        case -1   => NullArray.decoder
        case size => Array.decoder(size)
      },
    )

  val decoderNoTag: Codec[MultiBulkResponse] =
    constant(Tag) ~> decoder
}
