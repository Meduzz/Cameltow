package se.chimps.cameltow.framework.feaures

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.form.EagerFormParsingHandler
import se.chimps.cameltow.framework.Feature

object ParseForms {
  def apply():ParseForms = new ParseForms
}

class ParseForms extends Feature {
  private var next:HttpHandler = _

  override def httpHandler:HttpHandler = {
    val handler = new EagerFormParsingHandler()
    handler.setNext(next)
  }

  override def setNext(handler: HttpHandler):Unit = next = handler
}