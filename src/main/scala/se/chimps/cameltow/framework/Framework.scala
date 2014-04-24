package se.chimps.cameltow.framework

import se.chimps.cameltow.logging.Logging

/**
 * Created by meduzz on 24/04/14.
 */
trait Framework extends Logging {
  def apply()

  protected def get(path:String, handler:Function[String, Unit]) = {
    logger.info("GET handler added to path: {}.", path);
  }
}
