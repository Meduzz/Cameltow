package se.chimps.cameltow.framework.common

import se.chimps.cameltow.framework.old.BasicAction
import se.chimps.cameltow.framework.{Request, Response, ResponseImpl, old}
import se.chimps.cameltow.framework.old.ResponseBuilders.{Error, Ok}
import se.chimps.cameltow.framework.parsing.Decoder

/**
 * Created by meduzz on 24/05/14.
 */
trait TestAction extends BasicAction {
  def apply(valid:Boolean):old.Response
}

object TestAction {
  def apply(validator:old.Request=>Boolean):TestAction = new TestAction {
    override def apply(valid:Boolean):old.Response = {
      if (valid) {
        Ok().build()
      } else {
        Error(new TestException("Request was not valid!"))
      }
    }

    override def apply(request:old.Request):old.Response = this(validator(request))
  }
}

class TestException(val msg:String) extends Exception(msg) { }

object StringDecoder extends Decoder[String] {
  override def apply(entity:String):Option[Array[Byte]] = {
    Some(entity.getBytes("utf-8"))
  }
}