package se.chimps.cameltow.framework

import io.undertow.server.HttpHandler
import se.chimps.cameltow.framework.handlers.Action
import scala.concurrent.ExecutionContext.Implicits.global

object Handler {
  val NOT_FOUND:Handler = Action.sync(request => Response(404))
  val BAD_REQUEST:Handler = Action.sync(request => Response(400))
}

trait Handler {
  def httpHandler:HttpHandler
}