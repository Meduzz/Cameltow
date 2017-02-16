package se.chimps.cameltow.framework.handlers

import io.undertow.server.handlers.ResponseCodeHandler
import io.undertow.server.{HttpHandler, HttpServerExchange}
import io.undertow.util.{HttpString, Methods => M}
import se.chimps.cameltow.framework.Handler

object Methods {

  def GET(handler:Handler):Handler = new Methods(M.GET, handler)
  def POST(handler:Handler):Handler = new Methods(M.POST, handler)
  def PUT(handler:Handler):Handler = new Methods(M.PUT, handler)
  def DELETE(handler:Handler):Handler = new Methods(M.DELETE, handler)
  def HEAD(handler:Handler):Handler = new Methods(M.HEAD, handler)
  def PATCH(handler:Handler):Handler = new Methods(HttpString.tryFromString("PATCH"), handler)

}

class Methods(val method:HttpString, val next:Handler) extends Handler {
  override private[cameltow] def httpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange):Unit = {
      if (exchange.getRequestMethod.equals(method)) {
        next.httpHandler.handleRequest(exchange)
      } else {
        ResponseCodeHandler.HANDLE_404.handleRequest(exchange)
      }
    }
  }
}