package se.chimps.cameltow.framework.dependencyinjection

import com.google.inject.{Module, Guice => Juice}
import collection.JavaConversions._

/**
 * Created by meduzz on 16/05/14.
 */
trait Guice extends DIBase {
  private var modules:Array[_<:Module] = Array()
  private lazy val injector = Juice.createInjector(asJavaIterable(modules.toIterable))

  override def instance[T >: K, K](clazz: Class[K]): T = injector.getInstance(clazz)

  def injectable(module: Module): Unit = {
    modules = modules ++ Array(module)
  }
}