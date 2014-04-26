package se.chimps.cameltow.framework.parsing

/**
 * Created by meduzz on 25/04/14.
 */
trait Decoder[T] {
  def apply(entity:T):Option[Array[Byte]]
}
