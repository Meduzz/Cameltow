package se.chimps.cameltow.framework.handlers

import java.net.URI

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.proxy
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient
import se.chimps.cameltow.framework.Handler

object ProxyHandler {
  def delegate():ProxyDelegate = new ProxyDelegate
  def apply(delegate: ProxyDelegate, requestTimeoutMs:Int = 3000, fallback:Handler = Handler.UNAVAILABLE):ProxyHandler = new ProxyHandler(delegate, requestTimeoutMs, fallback)
}

class ProxyDelegate {
  val client = new LoadBalancingProxyClient()

  def addHost(url:String):Unit = client.addHost(new URI(url))
  def removeHost(url:String):Unit = client.removeHost(new URI(url))
}

class ProxyHandler(val delegate: ProxyDelegate, val requestTimeoutMs:Int, val fallback:Handler) extends Handler {
  override def httpHandler: HttpHandler = {
    new proxy.ProxyHandler(delegate.client, requestTimeoutMs, fallback.httpHandler)
  }
}