package example

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework.Framework

/**
 * Created by meduzz on 24/04/14.
 */
object Server extends Cameltow {
  def initialize() = {
    registerController(Controller)
  }
}

object Controller extends Framework {
  override def apply() {
    get("/asdf", (in:String)=>logger.info(in))
  }
}