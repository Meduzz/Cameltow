package se.chimps.cameltow.framework.feaures

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.IPAddressAccessControlHandler
import se.chimps.cameltow.framework.Feature

object IpAclFeature {
  private var delegate = new IpDelegate

  def apply():IpAclFeature = new IpAclFeature(delegate)

  def getDelegate:IpDelegate = delegate
}

class IpDelegate {
  private var handlers:Seq[IPAddressAccessControlHandler] = Seq()

  def allow(ip:String):Unit = handlers.foreach(_.addAllow(ip))
  def deny(ip:String):Unit = handlers.foreach(_.addDeny(ip))
  def defaultAllow(torf:Boolean):Unit = handlers.foreach(_.setDefaultAllow(torf))

  def apply(handler:IPAddressAccessControlHandler):Unit = {
    handlers = handlers ++ Seq(handler)
  }
}

class IpAclFeature(delegate: IpDelegate) extends Feature {
  val httpHandler = new IPAddressAccessControlHandler()
  override def setNext(newNext: HttpHandler): Unit = httpHandler.setNext(newNext)

  delegate(httpHandler)
}
