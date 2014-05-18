package se.chimps.cameltow.framework.dependencyinjection

import com.google.inject.{Module, Guice, AbstractModule, Injector}
import se.chimps.cameltow.framework.lifecycle.Lifecycle

/**
 * Created by meduzz on 16/05/14.
 */
trait Guice extends DIBase {
  private val module = new SpritsModule()
  private val injector = Guice.createInjector(module)

  override def instance[T >: K, K](clazz: Class[K]): T = injector.getInstance(clazz)

  override def injectable(jsr303: Class[_]): Unit = {
    module.doBind(jsr303)

    if (jsr303.isInstanceOf[Lifecycle]) {
      registerLifecycle(injector.getInstance(jsr303).asInstanceOf[Lifecycle])
    }
  }

  def install(aModule:AbstractModule):Unit = module.doInstall(aModule)
}

private class SpritsModule extends AbstractModule {
  override def configure(): Unit = {}

  def doBind(clazz:Class[_]):Unit = {
    bind(clazz)
  }

  def doInstall(module:Module):Unit = {
    install(module)
  }
}