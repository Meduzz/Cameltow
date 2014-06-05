package se.chimps.cameltow.framework.dependencyinjection

import org.scalatest.FunSuite
import se.chimps.cameltow.framework.dependencyinjection.util.{SpringTestApp, GuiceTestApp}
import se.chimps.cameltow.framework.dependencyinjection.util.Common.LifecycleImpl

/**
 * Created by meduzz on 05/06/14.
 */
class DITest extends FunSuite {
  test("guice registers lifecycle") {
    val guice = new GuiceTestApp
    val lf = new LifecycleImpl
    guice.registerLifecycle(lf)

    assert(guice.lifecycle(0).equals(lf))
  }

  test("spring registers lifecycle") {
    val spring = new SpringTestApp
    val lf = new LifecycleImpl
    spring.registerLifecycle(lf)

    assert(spring.lifecycle(0).equals(lf))
  }
}