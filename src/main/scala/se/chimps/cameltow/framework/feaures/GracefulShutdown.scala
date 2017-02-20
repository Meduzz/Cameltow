package se.chimps.cameltow.framework.feaures

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.GracefulShutdownHandler
import se.chimps.cameltow.framework.Feature

object GracefulShutdown {
  def apply(): GracefulShutdown = new GracefulShutdown
}

class GracefulShutdown extends Feature {
  private var next:HttpHandler = _

  override def httpHandler:HttpHandler = new GracefulShutdownHandler(next)

  override def setNext(handler: HttpHandler):Unit = next = handler
}