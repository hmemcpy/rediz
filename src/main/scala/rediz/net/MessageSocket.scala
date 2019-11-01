package rediz.net

import rediz._
import rediz.net.message.BackendMessage
import rediz.protocol.RedisMessage
import scodec.codecs._
import zio.{Managed, Runtime, Task}

import scala.concurrent.duration._

trait MessageSocket {
  def receive: Task[BackendMessage]
  def send[A: RedisMessage](a: A): Task[Unit]
}

object MessageSocket {
  def apply(
      host: String,
      port: Int,
      debug: Boolean,
      readTimeout: FiniteDuration = 1.second,
      writeTimeout: FiniteDuration = 1.second,
  )(implicit runtime: Runtime[Any]): Managed[Throwable, MessageSocket] =
    BitVectorSocket(host, port, readTimeout, writeTimeout).map { bvs =>
      fromBitVectorSocket(bvs, 8192, debug)
    }

  def fromBitVectorSocket(
      bvs: BitVectorSocket,
      nBytes: Int,
      debug: Boolean,
  ): MessageSocket =
    new MessageSocket {
      override def receive: Task[BackendMessage] =
        for {
          bits    <- bvs.read(1)
          tag     = byte.decodeValue(bits).require
          decoder = BackendMessage.decoder(tag)
          b       <- bvs.read(nBytes - 1)
          _       <- printBytes(tag, b).when(debug)
        } yield decoder.decodeValue(b).require

      override def send[A](a: A)(implicit ev: RedisMessage[A]): Task[Unit] =
        bvs.write(ev.encoder.encode(a).require) *> Task.unit
    }
}
