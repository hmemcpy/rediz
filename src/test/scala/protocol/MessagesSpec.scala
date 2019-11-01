package protocol

import org.specs2._
import rediz.net.message._
import scodec.bits._

class MessagesSpec extends Specification { def is =
  s2"""
   Redis protocol messages specifications (https://redis.io/topics/protocol)
     StringReply                   ${resp("+OK\r\n") must_=== SimpleString("OK")}
     ErrorReply                    ${resp("-ERR something happened\r\n") must_=== Error("ERR", "something happened")}
     IntegerReply                  ${resp(":42\r\n") must_=== Integer(42)}
     IntegerReply negative number  ${resp(":-10\r\n") must_=== Integer(-10)}
     BulkReply String              ${resp("$6\r\nfoobar\r\n") must_=== BulkString("foobar")}
     BulkReply Null                ${resp("$-1\r\n") must_=== NullBulkString}
   Arrays
     Null array                    ${resp("*-1\r\n") must_=== NullArray}
     Empty                         ${resp("*0\r\n") must_=== Array(Nil)}
     with bulk string              ${resp("*1\r\n$3\r\nfoo\r\n") must_=== Array(List(BulkString("foo")))}
     mixed bulk, int, string       $arrayMixedStringInt

"""

  val arrayMixedStringInt = {
    resp("*3\r\n$3\r\nfoo\r\n:1\r\n+Hello\r\n") must_=== Array(
      List(
        BulkString("foo"),
        Integer(1),
        SimpleString("Hello")))
  }

  def resp(s: String): BackendMessage = {
    val bytes = ByteVector(s.getBytes)
    val tag= bytes.get(0)
    BackendMessage.decoder(tag).decodeValue(bytes.drop(1).toBitVector).require
  }
}
