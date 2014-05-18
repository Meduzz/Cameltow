package se.chimps.cameltow.framework.lifecycle

/**
 * Created by meduzz on 16/05/14.
 */
trait Lifecycle {
  def start():Unit
  def stop():Unit
}
