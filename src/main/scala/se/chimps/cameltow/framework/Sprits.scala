package se.chimps.cameltow.framework

import se.chimps.cameltow.framework.dependencyinjection.DIBase

/**
 * Created by meduzz on 24/04/14.
 */
trait Sprits {

  /**
   * Will start using the controller right away.
   * @param controller
   */
  def registerController(controller:SpritsController) = {
    // atm injections will be hard(er) to handle in this case, saving it for the future.
    controller()
  }

  /**
   * If the framework have IoC available, then we'll assume that the controller are available in the IoC container.
   * Else we'll run a newInstance on the class.
   * Then start using the controller.
   * @param controller
   */
  def registerController(controller:Class[SpritsController]) = {
    val ctrl = if (this.isInstanceOf[DIBase]) {
      // Has DI, assume Controller are part of DI.
      this.asInstanceOf[DIBase].instance(controller) // should take care of injections...
    } else {
      // No DI
      controller.newInstance()
    }

    ctrl()
  }
}
