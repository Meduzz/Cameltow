package se.chimps.cameltow.framework.routes

import io.undertow.server.RoutingHandler
import io.undertow.util.Methods
import se.chimps.cameltow.framework.Handler

trait Routes {

  def backingHandler:RoutingHandler

  def GET(path:String, handler:Handler):Unit = backingHandler.get(path, handler.httpHandler)
  def POST(path:String, handler:Handler):Unit = backingHandler.post(path, handler.httpHandler)
  def PUT(path:String, handler:Handler):Unit = backingHandler.put(path, handler.httpHandler)
  def DELETE(path:String, handler:Handler):Unit = backingHandler.delete(path, handler.httpHandler)
  def HEAD(path:String, handler:Handler):Unit = backingHandler.add(Methods.HEAD, path, handler.httpHandler)
  def PATCH(path:String, handler:Handler):Unit = backingHandler.add("PATCH", path, handler.httpHandler)

  def routes(path:String):Routes = {
    new RoutesImpl(backingHandler)
  }

  def handler:Handler
}

private[cameltow] class RoutesImpl(val backingHandler:RoutingHandler) extends Routes {
  override def handler: Handler = new Handler {
    override private[cameltow] def httpHandler = backingHandler
  }
}