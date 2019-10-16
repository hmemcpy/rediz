package rediz.net

import java.net.InetSocketAddress

import cats.effect.{Blocker, Resource}
import fs2.Chunk
import fs2.io.tcp._
import scodec.bits.BitVector
import zio.interop.catz._
import zio.{Managed, Runtime, Task}

import scala.concurrent.duration.FiniteDuration

trait BitVectorSocket {
  def write(bits: BitVector): Task[Unit]
  def read(nBytes: Int): Task[BitVector]
}

object BitVectorSocket {
  def apply(
      host: String,
      port: Int,
      readTimeout: FiniteDuration,
      writeTimeout: FiniteDuration
  )(implicit runtime: Runtime[Any]): Managed[Throwable, BitVectorSocket] =
    Socket(host, port).map { sock =>
      fromSocket(sock, readTimeout, writeTimeout)
    }

  def fromSocket(
      socket: Socket[Task],
      readTimeout: FiniteDuration,
      writeTimeout: FiniteDuration
  ): BitVectorSocket =
    new BitVectorSocket {

      def readBytes(n: Int): Task[Array[Byte]] =
        socket.read(n, Some(readTimeout)).flatMap {
          case None => Task.fail(new Exception("Fatal: EOF"))
          case Some(c) =>
            if (c.size == 0) Task.fail(new Exception("Fatal: no bytes were read."))
            if (c.size <= n) Task.succeed(c.toArray)
            else Task.fail(new Exception(s"Fatal: EOF before $n bytes could be read."))
        }

      override def read(nBytes: Int): Task[BitVector] =
        readBytes(nBytes).map(BitVector(_))

      override def write(bits: BitVector): Task[Unit] =
        socket.write(Chunk.array(bits.toByteArray), Some(writeTimeout))
    }
}

object Socket {
  def apply(
      host: String,
      port: Int
  )(implicit runtime: Runtime[Any]): Managed[Throwable, Socket[Task]] =
    client(new InetSocketAddress(host, port)).toManaged

  def client(to: InetSocketAddress): Resource[Task, Socket[Task]] =
    Blocker[Task].flatMap { blocker =>
      SocketGroup[Task](blocker).flatMap { sg =>
        sg.client[Task](to)
      }
    }
}
