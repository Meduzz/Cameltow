package se.chimps.cameltow.framework.dependencyinjection.util

import se.chimps.cameltow.framework.dependencyinjection.{Spring, DIBase}
import scala.collection.mutable
import se.chimps.cameltow.framework.lifecycle.Lifecycle

/**
 * Created by meduzz on 05/06/14.
 */
class SpringTestApp extends Spring {
  val lifecycle:mutable.MutableList[Lifecycle] = mutable.MutableList()
}
