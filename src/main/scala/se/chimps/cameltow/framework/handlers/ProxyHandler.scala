package se.chimps.cameltow.framework.handlers

import java.net.URI

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.proxy
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient
import se.chimps.cameltow.framework.Handler

object ProxyHandler {
  def delegate():ProxyDelegate = new ProxyDelegate
  def apply(delegate: ProxyDelegate, fallback:Handler = Handler.UNAVAILABLE):ProxyHandler = new ProxyHandler(delegate, fallback)
}

class ProxyDelegate {
  val client = new LoadBalancingProxyClient()

  def addHost(url:String):Unit = client.addHost(new URI(url))
}

class ProxyHandler(val delegate: ProxyDelegate, val fallback:Handler) extends Handler {
  override def httpHandler: HttpHandler = {
    new proxy.ProxyHandler(delegate.client, 3, fallback.httpHandler)
  }
}