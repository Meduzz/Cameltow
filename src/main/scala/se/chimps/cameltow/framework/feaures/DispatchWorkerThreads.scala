package se.chimps.cameltow.framework.feaures

import io.undertow.server.{HttpHandler, HttpServerExchange}
import se.chimps.cameltow.framework.Feature

object DispatchWorkerThreads {
  def apply():Feature = new DispatchWorkerThreads
}

class DispatchWorkerThreads extends Feature {
  private var next:HttpHandler = _

  override def httpHandler: HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange):Unit = {
      if (exchange.isInIoThread) {
        exchange.dispatch(next)
      } else {
        next.handleRequest(exchange)
      }
    }
  }

  override def setNext(httpHandler: HttpHandler): Unit = next = httpHandler
}