package se.chimps.cameltow.framework.routes.handlers

import io.undertow.server.{HttpHandler, HttpServerExchange}
import se.chimps.cameltow.framework.routes.Handler
import se.chimps.cameltow.framework.{Request, Response}

import scala.concurrent.{ExecutionContext, Future}

object Action {
  def apply(handler:(Request)=>Future[Response])(implicit ec:ExecutionContext):Handler = new FutureHandler(handler)
}

class FutureHandler(val func:(Request)=>Future[Response])(implicit ec:ExecutionContext) extends Handler {
  override private[cameltow] def httpHandler: HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange):Unit = {
      func(Request(exchange)).foreach(resp => resp.write(exchange))
    }
  }
}