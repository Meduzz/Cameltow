package se.chimps.cameltow.framework

import java.nio.ByteBuffer

import io.undertow.server.HttpServerExchange
import io.undertow.util.{Headers, HttpString}

import scala.concurrent.ExecutionContext

case class Response(code:Int, headers:Map[String, String] = Map(), body:Option[ResponseBody[_]] = None) {

  def withHeader(header:String, value:String):Response =
    copy(headers = headers ++ Map(header -> value))

  def withHeaders(additional:Map[String, String]):Response =
    copy(headers = headers ++ additional)

  def withBody(data:ResponseBody[_]):Response =
    copy(body = Some(data))

  private[cameltow] def write(exchange:HttpServerExchange)(implicit ec:ExecutionContext) = {
    exchange.setStatusCode(code)

    headers.foreach(kv => {
      val (header, value) = kv
      exchange.getResponseHeaders.put(HttpString.tryFromString(header), value)
    })

    body match {
      case Some(t:StringResponseBody) => {
        t.contentType.map(ct => exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, ct))
        exchange.getResponseSender.send(t())
      }
      case Some(t:ByteResponseBody) => {
        t.contentType.map(ct => exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, ct))
        exchange.getResponseSender.send(ByteBuffer.wrap(t()))
      }
      case None =>
    }
  }
}