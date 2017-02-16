package se.chimps.cameltow.framework.handlers

import java.util.concurrent.TimeUnit

import io.undertow.server.{HttpHandler, HttpServerExchange}
import se.chimps.cameltow.framework.{Handler, Request, Response}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

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
      Await.ready(func(Request(exchange)), Duration(3L, TimeUnit.SECONDS)).value.get match {
        case Success(resp) => resp.write(exchange)
        case Failure(e) => throw e
      }
    }
  }
}