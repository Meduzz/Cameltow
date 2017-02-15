package se.chimps.cameltow.framework.handlers

import java.util.concurrent.TimeUnit

import io.undertow.server.handlers.ResponseRateLimitingHandler
import se.chimps.cameltow.framework.Handler

object ResponseRateLimit {
  def apply(kbPerSecond: Int, next: Handler): ResponseRateLimit = new ResponseRateLimit(kbPerSecond, next)
}

class ResponseRateLimit(val kbPerSecond:Int, next:Handler) extends Handler {
  override private[cameltow] def httpHandler =
    new ResponseRateLimitingHandler(next.httpHandler, kbPerSecond * 1024, 1, TimeUnit.SECONDS)
}