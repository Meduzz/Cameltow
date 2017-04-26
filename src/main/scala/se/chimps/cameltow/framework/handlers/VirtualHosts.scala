package se.chimps.cameltow.framework.handlers

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.NameVirtualHostHandler
import se.chimps.cameltow.framework.Handler

object VirtualHosts {
  def apply():VirtualHosts = new VirtualHosts()
}

class VirtualHosts {
  private val parent = new NameVirtualHostHandler()
  parent.setDefaultHandler(Handler.NOT_FOUND.httpHandler)

  def addHandler(host:String, handler:Handler):Unit = parent.addHost(host, handler.httpHandler)

  def removeHandler(host:String):Unit = parent.removeHost(host)

  def handler:Handler = new Handler {
    override def httpHandler:HttpHandler = parent
  }
}
