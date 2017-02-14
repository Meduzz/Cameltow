package se.chimps.cameltow.framework

import io.undertow.server.HttpHandler

trait Handler {
  private[cameltow] def httpHandler:HttpHandler
}