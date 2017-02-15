package se.chimps.cameltow.framework.handlers

import io.undertow.server.{HttpHandler, HttpServerExchange}
import se.chimps.cameltow.framework.{Handler, Request, Response}

import scala.concurrent.{ExecutionContext, Future}

object Action {
  def apply(handler:(Request)=>Future[Response])(implicit ec:ExecutionContext):Handler = new FutureHandler(handler)

  def sync(handler:(Request)=>Response)(implicit ec:ExecutionContext):Handler = new Handler {
    override private[cameltow] def httpHandler = new HttpHandler {
      override def handleRequest(exchange: HttpServerExchange):Unit =
        handler(Request(exchange)).write(exchange)
    }
  }
}

class FutureHandler(val func:(Request)=>Future[Response])(implicit ec:ExecutionContext) extends Handler {
  override private[cameltow] def httpHandler: HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange):Unit = {
      func(Request(exchange)).foreach(resp => resp.write(exchange))
    }
  }
}