package se.chimps.cameltow.framework

import io.undertow.server.HttpServerExchange
import io.undertow.util.{Headers, HttpString}

import scala.concurrent.ExecutionContext

case class Response(code:Int, headers:Map[String, String] = Map(), body:Option[ResponseBody[_]] = None, cookie:Option[Cookie] = None) {

  def withHeader(header:String, value:String):Response =
    copy(headers = headers ++ Map(header -> value))

  def withHeaders(additional:Map[String, String]):Response =
    copy(headers = headers ++ additional)

  def withBody(data:ResponseBody[_]):Response =
    copy(body = Some(data))

  def withCookie(cookie: Cookie):Response =
    copy(cookie = Some(cookie))

  private[cameltow] def write(exchange:HttpServerExchange)(implicit ec:ExecutionContext) = {
    exchange.setStatusCode(code)

    cookie match {
      case Some(cookie:Cookie) => exchange.setResponseCookie(cookie.undertowCookie)
      case None =>
    }

    headers.foreach(kv => {
      val (header, value) = kv
      exchange.getResponseHeaders.put(HttpString.tryFromString(header), value)
    })

    body match {
      case Some(t:Text) => {
        t.contentType.map(ct => exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, ct))
        exchange.getResponseSender.send(t.content)
      }
      case Some(h:Html) => {
        h.contentType.map(ct => exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, ct))
        exchange.getResponseSender.send(h.content)
      }
      case Some(j:Json) => {
        j.contentType.map(ct => exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, ct))
        exchange.getResponseSender.send(j.content)
      }
      case None =>
      case _ => // TODO do we need a way to inject stuff here?
    }
  }
}