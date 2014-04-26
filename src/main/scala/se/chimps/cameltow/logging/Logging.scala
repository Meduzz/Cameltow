package se.chimps.cameltow.logging

import org.slf4j.{Marker, LoggerFactory}

/**
 * Created by meduzz on 24/04/14.
 */
trait Logging {
  val logger = LoggerFactory.getLogger(this.getClass)
}
