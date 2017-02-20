package se.chimps.cameltow.framework.handlers

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.OriginHandler
import se.chimps.cameltow.framework.Handler

object Origin {
  def apply(requireOriginHeader: Boolean, origins: Seq[String], next:Handler, noOriginHandler:Handler = Handler.BAD_REQUEST): Origin = new Origin(requireOriginHeader, origins, next)
}

class Origin(requireOriginHeader:Boolean, origins:Seq[String], next:Handler) extends Handler {
  override def httpHandler:HttpHandler = {
    val handler = new OriginHandler()

    handler.setRequireOriginHeader(requireOriginHeader)
    handler.setRequireAllOrigins(false)
    origins.foreach(handler.addAllowedOrigin)
    handler.setNext(next.httpHandler)

    handler
  }
}