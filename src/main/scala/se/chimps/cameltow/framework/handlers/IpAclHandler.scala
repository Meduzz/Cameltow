package se.chimps.cameltow.framework.handlers

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.IPAddressAccessControlHandler
import se.chimps.cameltow.framework.Handler
import se.chimps.cameltow.framework.feaures.IpDelegate

object IpAclHandler {

  def delegate():IpDelegate = new IpDelegate

  def apply(delegate: IpDelegate, handler:Handler):Handler =
}

class IpAclHandler(delegate: IpDelegate) extends Handler {
  override def httpHandler: HttpHandler = {
    val handler = new IPAddressAccessControlHandler()
    delegate(handler)
    handler
  }
}
