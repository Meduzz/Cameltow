package se.chimps.cameltow.framework.routes

import io.undertow.server.HttpHandler

trait Handler {
  private[cameltow] def httpHandler:HttpHandler
}