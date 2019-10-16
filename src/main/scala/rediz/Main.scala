package rediz

import rediz.net.MessageSocket
import rediz.protocol.Ping
import zio._
import zio.console._
import zio.interop.catz._

object Main extends CatsApp {
  def run(args: List[String]) =
    program
      .foldM(t => Task.effectTotal(t.printStackTrace()) *> ZIO.succeed(1), _ => ZIO.succeed(0))

  val msg = Ping()

  def program =
    MessageSocket("localhost", 6379)
      .use { sock =>
        for {
          _  <- sock.send(msg)
          bm <- sock.receive
          _  <- putStrLn(bm.toString)
        } yield ()
      }
}


