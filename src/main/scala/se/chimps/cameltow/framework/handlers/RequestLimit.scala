package se.chimps.cameltow.framework.handlers

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.RequestLimitingHandler
import se.chimps.cameltow.framework.Handler

object RequestLimit {
  def apply(maxConcurrentRequests: Int, next: Handler): RequestLimit = new RequestLimit(maxConcurrentRequests, next)
}

class RequestLimit(val maxConcurrentRequests:Int, val next:Handler) extends Handler {
  override def httpHandler:HttpHandler =
    new RequestLimitingHandler(maxConcurrentRequests, next.httpHandler)
}