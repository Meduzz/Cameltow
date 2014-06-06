package se.chimps.cameltow.di.util

import com.google.inject.AbstractModule
import se.chimps.cameltow.di.util.Common.{LifecycleImpl, Dummy}
import se.chimps.cameltow.lifecycle.Lifecycle

/**
 * Created by meduzz on 05/06/14.
 */
class GuiceTestModule extends AbstractModule {
  override def configure():Unit = {
    bind(classOf[Dummy]).toConstructor(classOf[Dummy].getConstructor(classOf[Lifecycle]))
    bind(classOf[Lifecycle]).to(classOf[LifecycleImpl])
  }
}
