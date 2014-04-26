package example

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework._

/**
 * Created by meduzz on 24/04/14.
 */
object Server extends Cameltow {
  def initialize() = {
    registerController(Controller)
    listen(8080)
  }
}

object Controller extends Sprits {
  import ResponseBuilders.RestResponseBuilder._
  import ResponseBuilders.Error

  override def apply() {
    get("/asdf", BasicAction {
      Ok().build()
    })

    get("/qwer", Authorized { auth =>
      if (auth) {
        Ok().build()
      } else {
        Error()
      }
    }(req => true))
  }
}

trait Authorized extends BasicAction {
  def apply(authed:Boolean):Response
}

object Authorized {
  def apply(ctrl:Boolean=>Response)(f:Request=>Boolean):Authorized = new Authorized {
    override def apply(authed:Boolean):Response = ctrl(authed)

    override def apply(req:Request):Response = this(f(req))
  }
}