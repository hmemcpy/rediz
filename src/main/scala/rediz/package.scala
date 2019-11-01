import scodec.bits.BitVector
import zio.ZIO

package object rediz {
  def printBytes(tag: Byte, bv: BitVector): ZIO[Any, Nothing, Unit] = {
    val s = s"DEBUG: ${tag.toChar} ${bv.toByteVector.toHex}"
    ZIO.effectTotal(println(s"⚡ ${scala.Console.YELLOW}$s${scala.Console.RESET}"))
  }
}
