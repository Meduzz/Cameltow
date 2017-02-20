package se.chimps.cameltow.framework.feaures

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.encoding.EncodingHandler
import se.chimps.cameltow.framework.Feature

object Gzip {
  // TODO come up with a way to extend this with new codecs.
  def apply(): Gzip = new Gzip()
}

class Gzip extends Feature {
  private var next:HttpHandler = _

  override def httpHandler:HttpHandler = new EncodingHandler.Builder().build(null).wrap(next)

  override def setNext(handler: HttpHandler):Unit = next = handler
}