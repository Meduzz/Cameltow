package se.chimps.cameltow.framework

/**
 * Created by meduzz on 24/04/14.
 */
trait Framework {

  // Config and builder are available.

  def registerController(controller:Sprits) = {
    controller()
  }

  def staticContent(baseUrl:String, path:String) = {
    // TODO implement
  }
}
