package se.chimps.cameltow.framework.dependencyinjection

import se.chimps.cameltow.framework.lifecycle.Lifecycle
import scala.collection.mutable

/**
 * Created by meduzz on 16/05/14.
 */
trait DIBase {
  def lifecycle:mutable.Seq[Lifecycle]

  def instance[T>:K, K](clazz:Class[K]):T

  def injectable(jsr303:Class[_]):Unit

  protected def registerLifecycle(lc:Lifecycle):Unit = {
    lifecycle :+ mutable.Seq(lc)
  }
}
