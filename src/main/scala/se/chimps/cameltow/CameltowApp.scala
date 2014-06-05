package se.chimps.cameltow

import se.chimps.cameltow.logging.Logging
import scala.collection.mutable
import se.chimps.cameltow.lifecycle.Lifecycle

/**
 * Created by meduzz on 06/06/14.
 */
trait CameltowApp extends Logging {

  val lifecycle:mutable.MutableList[Lifecycle] = mutable.MutableList()

  def registerLifecycle(bean:Lifecycle) = {
    lifecycle += bean
  }

  def start() = {
    lifecycle.foreach { lc =>
      lc.start()
    }
  }

  def stop() = {
    lifecycle.foreach { lc =>
      lc.stop()
    }
  }

}
