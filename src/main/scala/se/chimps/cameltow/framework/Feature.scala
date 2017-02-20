package se.chimps.cameltow.framework

import io.undertow.server.HttpHandler

trait Feature {
  def httpHandler:HttpHandler
  def setNext(httpHandler: HttpHandler)
}