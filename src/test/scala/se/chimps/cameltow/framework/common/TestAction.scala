package se.chimps.cameltow.framework.common

import se.chimps.cameltow.framework.{ResponseImpl, Response, Request, BasicAction}
import se.chimps.cameltow.framework.ResponseBuilders.{Error, Ok}

/**
 * Created by meduzz on 24/05/14.
 */
trait TestAction extends BasicAction {
  def apply(valid:Boolean):Response
}

object TestAction {
  def apply(validator:Request=>Boolean):TestAction = new TestAction {
    override def apply(valid:Boolean):Response = {
      if (valid) {
        Ok().build()
      } else {
        Error(new TestException("Request was not valid!"))
      }
    }

    override def apply(request:Request):Response = this(validator(request))
  }
}

class TestException(val msg:String) extends Exception(msg) { }