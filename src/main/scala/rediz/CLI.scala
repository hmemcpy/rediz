package rediz

import rediz.net.MessageSocket
import rediz.protocol.RedisCommand
import zio._
import zio.console._

object CLI {
  def apply(host: String, port: Int, debug: Boolean)(implicit ev: Runtime[Any]) = {
    MessageSocket(host, port, debug)
      .use { sock =>
        (for {
          _     <- putStr(s"$host:$port> ")
          input <- getStrLn
          cmd <- parse(input) match {
                  case None      => ZIO.fail(s"Unable to parse the command $input")
                  case Some(cmd) => ZIO.succeed(cmd)
                }
          _  <- sock.send(cmd)
          bm <- sock.receive
          _  <- putStrLn(bm.toString)
        } yield ()).catchAll(_ => ZIO.unit).forever
      }
  }

  def parse(s: String): Option[RedisCommand] = {
    // split by space, except when in quotes
    s.trim.split("\\s(?=([^\"]*\"[^\"]*\")*[^\"]*$)").toList match {
      case h :: t if !h.isEmpty => Some(RedisCommand(h, t: _*))
      case _                    => None
    }
  }
}
