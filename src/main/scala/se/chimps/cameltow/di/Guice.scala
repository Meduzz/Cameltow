package se.chimps.cameltow.di

import com.google.inject.{Guice => Juice, Injector, Module}
import collection.JavaConversions._
import se.chimps.cameltow.modules.DependencyInjection
import se.chimps.cameltow.lifecycle.Lifecycle
import scala.collection.mutable
import se.chimps.cameltow.exceptions.NotStartedException
import se.chimps.cameltow.{CameltowApp, Cameltow}

/**
 * Created by meduzz on 16/05/14.
 */
trait Guice extends DependencyInjection { self:CameltowApp =>

  private val moduleLifecycle = new GuiceLifecycle
  self.registerLifecycle(moduleLifecycle)

  override def instance[T >: K, K](clazz: Class[K]): T = moduleLifecycle.getInstance(clazz)

  def addModule(module: Module): Unit = {
    moduleLifecycle.injectable(module)
  }
}

private class GuiceLifecycle extends Lifecycle {
  private val modules:mutable.MutableList[Module] = mutable.MutableList[Module]()
  private var injector:Option[Injector] = None

  def injectable(module: Module): Unit = {
    modules += module
  }

  override def start():Unit = injector = Some(Juice.createInjector(asJavaIterable(modules.toIterable)))

  override def stop():Unit = injector = None

  def getInstance[T >: K, K](clazz: Class[K]):T = injector match {
    case Some(injector:Injector) => injector.getInstance(clazz)
    case None => throw new NotStartedException("App has not started yet.")
  }
}