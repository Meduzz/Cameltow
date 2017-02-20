package se.chimps.cameltow.framework.handlers

import io.undertow.server.{HttpHandler, HttpServerExchange}
import se.chimps.cameltow.framework.{Handler, Request, Response}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

object Action {
  def apply(handler:(Request)=>Future[Response])(implicit ec:ExecutionContext, responseTimeout:Duration):Handler = new AsyncHandler(handler)

  def sync(handler:(Request)=>Response)(implicit ec:ExecutionContext):Handler = new SyncHandler(handler)
}

class AsyncHandler(val func:(Request)=>Future[Response])(implicit ec:ExecutionContext, responseTimeout:Duration) extends Handler {
  override def httpHandler: HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange):Unit = {
      Await.ready(func(Request(exchange)), responseTimeout).value.get match {
        case Success(resp) => resp.write(exchange)
        case Failure(e) => throw e
      }
    }
  }
}

class SyncHandler(val func:(Request)=>Response)(implicit ec:ExecutionContext) extends Handler {
  override def httpHandler: HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange):Unit =
      func(Request(exchange)).write(exchange)
  }
}