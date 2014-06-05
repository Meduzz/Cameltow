package se.chimps.cameltow.framework.dependencyinjection.util

import se.chimps.cameltow.framework.lifecycle.Lifecycle

/**
 * Created by meduzz on 05/06/14.
 */
object Common {

  class LifecycleImpl extends Lifecycle {
    var started = false
    var stopped = false

    override def start():Unit = started = true

    override def stop():Unit = stopped = true
  }

}