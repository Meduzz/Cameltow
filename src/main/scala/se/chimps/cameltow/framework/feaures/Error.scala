package se.chimps.cameltow.framework.feaures

import io.undertow.server.{HttpHandler, HttpServerExchange}
import org.slf4j.LoggerFactory
import se.chimps.cameltow.framework.{Feature, Response}

import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

// TODO using exchange.dispatch in a child-handler will break/circumvent this handler.
object Error {
  def apply(func:PartialFunction[Throwable, Response]):Error = new Error(func)
}

class Error(func:PartialFunction[Throwable, Response]) extends Feature {
  private var next:HttpHandler = _

  private val log = LoggerFactory.getLogger("cameltow.error")

  override private[cameltow] def httpHandler:HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange):Unit = {
      Try[Unit](next.handleRequest(exchange)) match { // is always a Success....
        case Failure(e) => {
          log.error("Caught exception:", e)
          func(e).write(exchange)
        }
        case s:Success[Unit] =>
      }
    }
  }

  override private[cameltow] def setNext(handler:HttpHandler):Unit = next = handler
}