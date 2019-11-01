package rediz

import zio.interop.catz._

object Main extends CatsApp {
  def run(args: List[String]) =
    REPL("localhost", 6379)
      .fold(_ => 1, _ => 0)

}
