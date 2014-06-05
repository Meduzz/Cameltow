package se.chimps.cameltow.di

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import se.chimps.cameltow.lifecycle.Lifecycle
import se.chimps.cameltow.modules.DependencyInjection
import se.chimps.cameltow.{CameltowApp}

/**
 * Created by meduzz on 16/05/14.
 */
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