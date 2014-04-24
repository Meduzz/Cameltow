package se.chimps.cameltow

import se.chimps.cameltow.servlets.Servlets
import se.chimps.cameltow.framework.{Framework, Sprits}
import se.chimps.cameltow.logging.Logging

/**
 * Created by meduzz on 22/04/14.
 */
abstract class Cameltow extends App with Logging with Servlets with Sprits {

  def initialize()

  if (logger.isDebugEnabled) {
    logger.debug("Starting Cameltow with these parameters: {}.", args)
  } else {
    logger.info("Starting Cameltow.")
  }

  initialize()
}