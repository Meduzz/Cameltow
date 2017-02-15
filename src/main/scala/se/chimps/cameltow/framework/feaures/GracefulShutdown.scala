package se.chimps.cameltow.framework.feaures

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.GracefulShutdownHandler
import se.chimps.cameltow.framework.Feature

object GracefulShutdown {
  def apply(): GracefulShutdown = new GracefulShutdown
}

class GracefulShutdown extends Feature {
  private var next:HttpHandler = _

  override private[cameltow] def httpHandler = new GracefulShutdownHandler(next)

  override private[cameltow] def setNext(handler: HttpHandler) = next = handler
}