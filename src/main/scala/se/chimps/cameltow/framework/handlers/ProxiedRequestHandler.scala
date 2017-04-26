package se.chimps.cameltow.framework.handlers

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.ProxyPeerAddressHandler
import se.chimps.cameltow.framework.Handler

/**
  * Turn proxied headers where they belong.
  */
object ProxiedRequestHandler {
  def apply(handler:Handler):ProxiedRequestHandler = {
    new ProxiedRequestHandler(handler)
  }
}

class ProxiedRequestHandler(val child:Handler) extends Handler {
  override def httpHandler: HttpHandler = new ProxyPeerAddressHandler(child.httpHandler)
}