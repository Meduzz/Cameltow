package se.chimps.cameltow.framework.parsing

/**
 * Created by meduzz on 25/04/14.
 */
trait Encoder[T] {
  def apply(body:Array[Byte]):T
}
