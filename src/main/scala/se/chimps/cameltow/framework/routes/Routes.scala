package se.chimps.cameltow.framework.routes

import io.undertow.server.handlers.{PathHandler, PathTemplateHandler}
import se.chimps.cameltow.framework.Handler

trait Routes {
  def pathHandler:PathHandler

  // not as pretty as the original though, but working.
  def prefix(path:String, handler:Handler):Unit = pathHandler.addPrefixPath(path, handler.httpHandler)
  def exact(path:String, handler:Handler):Unit = pathHandler.addExactPath(path, handler.httpHandler)
  def template(path:String, handler: Handler):Unit = {
    val tpl = new PathTemplateHandler(true)
    tpl.add(path, handler.httpHandler)
    pathHandler.addPrefixPath("/", tpl)
  }

  def handler:Handler
}

private[cameltow] class RoutesImpl(val pathHandler: PathHandler) extends Routes {
  override def handler: Handler = new Handler {
    override private[cameltow] def httpHandler = pathHandler
  }
}