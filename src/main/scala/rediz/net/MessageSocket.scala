package rediz.net

import rediz.net.message.{BackendMessage, RedisMessage}
import zio.{Managed, Runtime, Task}
import scala.concurrent.duration._
import scodec.codecs._

trait MessageSocket {
  def receive: Task[BackendMessage]
  def send[A: RedisMessage](a: A): Task[Unit]
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
      nBytes: Int = 8192
  ): MessageSocket =
    new MessageSocket {

      override def receive: Task[BackendMessage] =
        bvs.read(1).flatMap { bits =>
          val header = byte
          val tag = header.decodeValue(bits).require
          val decoder = BackendMessage.decoder(tag)
          bvs.read(nBytes - 1).map(b => {
            decoder.decodeValue(b).require
          })
        }

      override def send[A](a: A)(implicit ev: RedisMessage[A]): Task[Unit] =
        bvs.write(ev.encoder.encode(a).require) *> Task.unit
    }
}
