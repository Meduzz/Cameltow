package se.chimps.cameltow.framework

import java.nio.ByteBuffer
import java.util.Date

import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.CookieImpl
import io.undertow.util.{Headers, HttpString}

import scala.concurrent.ExecutionContext

case class Response(code:Int, headers:Map[String, String] = Map(), cookies:Map[String, String] = Map(), clearCookies:Seq[String] = Seq(), body:Option[ResponseBody[_]] = None) {

  def withHeader(header:String, value:String):Response =
    copy(headers = headers ++ Map(header -> value))

  def withHeaders(additional:Map[String, String]):Response =
    copy(headers = headers ++ additional)

  def withBody(data:ResponseBody[_]):Response =
    copy(body = Some(data))

  def withCookie(name:String, value:String):Response =
    copy(cookies = cookies ++ Map(name -> value))

  def withClearCookie(name:String):Response =
    copy(clearCookies = clearCookies ++ Seq(name))

  private[cameltow] def write(exchange:HttpServerExchange)(implicit ec:ExecutionContext) = {
    exchange.setStatusCode(code)

    headers.foreach(kv => {
      val (header, value) = kv
      exchange.getResponseHeaders.put(HttpString.tryFromString(header), value)
    })

    clearCookies.foreach(name => {
      val cookie = new CookieImpl(name)
      cookie.setValue("")
      cookie.setMaxAge(0)
      cookie.setExpires(new Date(0))
      cookie.setPath("/")
      cookie.setHttpOnly(true)
      exchange.getResponseCookies.put(name, cookie)
    })

    cookies.foreach(kv => {
      val (key, value) = kv
      val cookie = new CookieImpl(key, value)
      cookie.setPath("/")
      cookie.setHttpOnly(true)
      exchange.getResponseCookies.put(key, cookie)
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