package se.chimps.cameltow.di.util

import se.chimps.cameltow.lifecycle.Lifecycle
import org.springframework.stereotype.Component
import javax.inject.Inject

/**
 * Created by meduzz on 05/06/14.
 */
object Common {

  @Component
  class LifecycleImpl extends Lifecycle {
    var started = false
    var stopped = false

    override def start():Unit = started = true

    override def stop():Unit = stopped = true
  }

  @Component
  class Dummy(@Inject val lifecycle:Lifecycle) { }

}