package se.chimps.cameltow

import io.undertow.Undertow
import io.undertow.server.handlers._
import io.undertow.server.handlers.form.EagerFormParsingHandler

/**
 * Created by meduzz on 22/04/14.
 */
abstract class Cameltow extends CameltowApp with App {
  val builder:Undertow.Builder = Undertow.builder()
  var server:Undertow = null

  val rootHandler:PathHandler = new PathHandler()
  val pathTemplateHandler:PathTemplateHandler = new PathTemplateHandler()
  val formHandler:EagerFormParsingHandler = new EagerFormParsingHandler()

  /**
   * Listens on 127.0.0.1:port.
   * Calls start on all registered Lifecycle instances
   * @param port
   */
  def listen(port:Int):Unit = {
    listen("127.0.0.1", port)
  }

  /**
   * Listens to host:port.
   * Calls start on all registered Lifecycle instances.
   * @param host
   * @param port
   */
  def listen(host:String, port:Int):Unit = {
    // TODO add support for websocket handlers in some neat way.
    rootHandler.addPrefixPath("/", pathTemplateHandler)

    // boots the lifecycle
    super.start()

    // TODO there really must be a better way....
    server = builder.setHandler(new CanonicalPathHandler(new HttpContinueReadHandler(formHandler.setNext(rootHandler)))).addHttpListener(port, host).build()

    server.start()
  }

  override def stop():Unit = {
    super.stop()

    server.stop()
  }

  def initialize()

  // def main(args:Array[String] starts here :)
  if (logger.isDebugEnabled) {
    logger.debug("Starting Cameltow with these parameters: {}.", args)
  } else {
    logger.info("Starting Cameltow.")
  }

  initialize()
}