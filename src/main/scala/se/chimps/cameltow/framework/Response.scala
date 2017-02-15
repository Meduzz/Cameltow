package se.chimps.cameltow.framework

import java.nio.ByteBuffer

import io.undertow.server.HttpServerExchange
import io.undertow.util.HttpString

import scala.concurrent.ExecutionContext

case class Response(code:Int, headers:Map[String, String] = Map(), body:Option[Body] = None, cookie:Option[Cookie] = None) {

  def withHeader(header:String, value:String):Response =
    copy(headers = headers ++ Map(header -> value))

  def withHeaders(additional:Map[String, String]):Response =
    copy(headers = headers ++ additional)

  def withBody(data:Body):Response =
    copy(body = Some(data))

  def withCookie(cookie: Cookie):Response =
    copy(cookie = Some(cookie))

  private[cameltow] def write(exchange:HttpServerExchange)(implicit ec:ExecutionContext) = {
    exchange.setStatusCode(code)

    cookie match {
      case Some(cookie:Cookie) => exchange.setResponseCookie(cookie.undertowCookie())
      case None =>
    }

    headers.foreach(kv => {
      val (header, value) = kv
      exchange.getResponseHeaders.put(HttpString.tryFromString(header), value)
    })

    body match {
      case Some(Encoded(bytes)) => {
        bytes.foreach(data => {
          val buffer = ByteBuffer.wrap(data)
          exchange.getResponseSender.send(buffer)
        })
      }
      case Some(Stream(queue)) => {
        var data = queue.dequeue()
        while (data.nonEmpty) {
          val buffer = ByteBuffer.wrap(data)
          exchange.getResponseChannel.write(buffer)
          data = queue.dequeue()
        }
        exchange.getResponseChannel.writeFinal(ByteBuffer.allocate(0))
      }
    }
  }

}