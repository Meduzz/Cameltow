package se.chimps.cameltow.framework

import io.undertow.server.HttpHandler

trait Feature {
  private[cameltow] def httpHandler:HttpHandler
}
