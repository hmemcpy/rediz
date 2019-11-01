package rediz

import zio.interop.catz._

object Main extends CatsApp {
  def run(args: List[String]) =
    CLI("localhost", 6379, debug = false)
      .fold(_ => 1, _ => 0)

}
