package se.chimps.cameltow.framework.feaures

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.OriginHandler
import se.chimps.cameltow.framework.{Feature}

object Origin {
  def apply(hosts:Seq[String]):Origin = {
    val o = new Origin
    hosts.foreach(o.addHost)
    o
  }
}

class Origin extends Feature {
  private val originHandler = new OriginHandler()

  originHandler.setRequireAllOrigins(false)
  originHandler.setRequireOriginHeader(true)

  override def httpHandler: HttpHandler = originHandler

  override def setNext(httpHandler: HttpHandler): Unit = originHandler.setNext(httpHandler)

  def addHost(host:String):Unit = originHandler.addAllowedOrigin(host)
  def clearHosts():Unit = originHandler.clearAllowedOrigins()
}
