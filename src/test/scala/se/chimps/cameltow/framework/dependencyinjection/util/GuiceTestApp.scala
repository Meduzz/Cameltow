package se.chimps.cameltow.framework.dependencyinjection.util

import se.chimps.cameltow.framework.dependencyinjection.{Guice, DIBase}
import scala.collection.mutable
import se.chimps.cameltow.framework.lifecycle.Lifecycle

/**
 * Created by meduzz on 05/06/14.
 */
class GuiceTestApp extends Guice {
  val lifecycle:mutable.MutableList[Lifecycle] = mutable.MutableList()
}
