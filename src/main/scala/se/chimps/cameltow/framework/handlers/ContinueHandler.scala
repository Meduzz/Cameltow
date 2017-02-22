package se.chimps.cameltow.framework.handlers

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.HttpContinueReadHandler
import se.chimps.cameltow.framework.Handler

object ContinueHandler {
  def apply(handler:Handler):Handler = new ContinueHandler(handler)
}

class ContinueHandler(val next:Handler) extends Handler {
  override def httpHandler: HttpHandler = new HttpContinueReadHandler(next.httpHandler)
}
