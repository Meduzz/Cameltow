package se.chimps.cameltow.di

import org.scalatest.FunSuite
import se.chimps.cameltow.di.util.{GuiceTestModule, SpringTestConfig, SpringTestApp, GuiceTestApp}
import se.chimps.cameltow.di.util.Common.{Dummy, LifecycleImpl}

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

  test("spring noms the config and like what it sees") {
    val spring = new SpringTestApp
    spring.registerSpringAnnotatedConfig(classOf[SpringTestConfig])
    spring.start()

    val dummy = spring.instance(classOf[Dummy])

    assert(dummy != null)
    assert(dummy.isInstanceOf[Dummy])

    assert(dummy.lifecycle != null)
    assert(dummy.lifecycle.isInstanceOf[LifecycleImpl])
  }

  test("guice accepts the module and smiles like the sun") {
    val guice = new GuiceTestApp
    guice.addModule(new GuiceTestModule)
    guice.start()

    val dummy = guice.instance(classOf[Dummy])

    assert(dummy != null)
    assert(dummy.isInstanceOf[Dummy])

    assert(dummy.lifecycle != null)
    assert(dummy.lifecycle.isInstanceOf[LifecycleImpl])
  }
}