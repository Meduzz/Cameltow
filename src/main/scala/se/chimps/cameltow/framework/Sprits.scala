package se.chimps.cameltow.framework

import org.slf4j.Logger

/**
 * Created by meduzz on 24/04/14.
 */
trait Sprits {

  def registerController(controller:Framework) = {
    controller()
  }
}
