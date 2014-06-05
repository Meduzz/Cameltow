package se.chimps.cameltow.modules

import se.chimps.cameltow.{CameltowApp}
import com.google.inject.{Guice => Juice, Injector, Module}
import se.chimps.cameltow.lifecycle.Lifecycle
import scala.collection.mutable
import scala.collection.JavaConversions._
import scala.Some
import se.chimps.cameltow.exceptions.NotStartedException
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Created by meduzz on 16/05/14.
 */
trait DependencyInjection { self:CameltowApp =>

  def instance[T>:K, K](clazz:Class[K]):T
}

object DependencyInjection {
  trait Guice extends DependencyInjection { self:CameltowApp =>

    private val moduleLifecycle = new GuiceLifecycle
    self.registerLifecycle(moduleLifecycle)

    override def instance[T >: K, K](clazz: Class[K]): T = moduleLifecycle.getInstance(clazz)

    def addModule(module: Module): Unit = {
      moduleLifecycle.injectable(module)
    }
  }

  trait Spring extends DependencyInjection { self:CameltowApp =>
    private val springLifecycle = new SpringLifecycle
    self.registerLifecycle(springLifecycle)

    def scan(packages:String):Unit = {
      springLifecycle.scan(packages)
    }

    def registerSpringAnnotatedConfig(configuration:Class[_]):Unit = {
      springLifecycle.registerConfig(configuration)
    }

    override def instance[T >: K, K](clazz: Class[K]): T = springLifecycle.getInstance(clazz)
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

  private class SpringLifecycle extends Lifecycle {
    val spring = new AnnotationConfigApplicationContext()

    def scan(packages:String) = {
      spring.scan(packages)
    }

    def registerConfig(config:Class[_]) = {
      spring.register(config)
    }

    def getInstance[T >: K, K](clazz: Class[K]): T = spring.getBean(clazz)

    override def start():Unit = {
      spring.refresh()

      if (spring.getBeanDefinitionCount > 0) {
        spring.start()
      }
    }

    override def stop():Unit = spring.stop()
  }
}