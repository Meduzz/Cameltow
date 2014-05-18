package se.chimps.cameltow.framework

import se.chimps.cameltow.framework.dependencyinjection.DIBase

/**
 * Created by meduzz on 24/04/14.
 */
trait Sprits {

  def registerController(controller:SpritsController) = {
    // atm injections will be hard(er) to handle in this case, saving it for the future.
    controller()
  }

  def registerController(controller:Class[SpritsController]) = {
    val ctrl = if (this.isInstanceOf[DIBase]) {
      // Has DI
      this.asInstanceOf[DIBase].injectable(controller)
      this.asInstanceOf[DIBase].instance(controller) // should take care of injections...
    } else {
      // No DI
      controller.newInstance()
    }

    ctrl()
  }
}
