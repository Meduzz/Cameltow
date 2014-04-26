package se.chimps.cameltow

import se.chimps.cameltow.servlets.Servlets
import se.chimps.cameltow.framework.{Sprits, Framework}
import se.chimps.cameltow.logging.Logging

/**
 * Created by meduzz on 22/04/14.
 */
abstract class Cameltow extends App with Logging with Servlets with Framework {
  // TODO implement a config... again
  // TODO look at adding simple support for Guice

  def initialize()

  if (logger.isDebugEnabled) {
    logger.debug("Starting Cameltow with these parameters: {}.", args)
  } else {
    logger.info("Starting Cameltow.")
  }

  initialize()

  def listen(port:Int):Unit = {
    listen("127.0.0.1", port)
  }

  def listen(host:String, port:Int):Unit = {
    // TODO implement
  }
}