package rediz.net

import rediz.net.message.{BackendMessage, FrontEndMessage}
import zio.{Managed, Runtime, Task}
import scala.concurrent.duration._

trait MessageSocket {
  def receive: Task[BackendMessage]
  def send[A: FrontEndMessage](a: A): Task[Unit]
}

object MessageSocket {
  def apply(
      host: String,
      port: Int,
      readTimeout: FiniteDuration = 1.second,
      writeTimeout: FiniteDuration = 1.second,
  )(implicit runtime: Runtime[Any]): Managed[Throwable, MessageSocket] =
    BitVectorSocket(host, port, readTimeout, writeTimeout).map { bvs =>
      fromBitVectorSocket(bvs)
    }

  def fromBitVectorSocket(
      bvs: BitVectorSocket,
      nBytes: Int = 1024
  ): MessageSocket =
    new MessageSocket {

      override def receive: Task[BackendMessage] = {
        bvs.read(nBytes).map { bits =>
          val bv = bits.toByteVector
          val tag = bv.get(0)
          BackendMessage.decoder(tag).decode(bv.drop(1).toBitVector).require.value
        }
      }

      override def send[A](a: A)(implicit ev: FrontEndMessage[A]): Task[Unit] =
        bvs.write(ev.encoder.encode(a).require) *> Task.unit
    }
}
