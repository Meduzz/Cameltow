package se.chimps.cameltow.di

import org.scalatest.FunSuite
import se.chimps.cameltow.di.util.{SpringTestApp, GuiceTestApp}
import se.chimps.cameltow.di.util.Common.LifecycleImpl

/**
 * Created by meduzz on 05/06/14.
 */
class DITest extends FunSuite {
  test("guice registers lifecycle") {
    val guice = new GuiceTestApp
    val lf = new LifecycleImpl
    guice.registerLifecycle(lf)
    guice.start()

    assert(lf.started)
  }

  test("spring registers lifecycle") {
    val spring = new SpringTestApp
    val lf = new LifecycleImpl
    spring.registerLifecycle(lf)
    spring.start()

    assert(lf.started)
  }
}