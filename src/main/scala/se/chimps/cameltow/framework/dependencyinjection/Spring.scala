package se.chimps.cameltow.framework.dependencyinjection

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import se.chimps.cameltow.framework.lifecycle.Lifecycle

/**
 * Created by meduzz on 16/05/14.
 */
trait Spring extends DIBase {
  private val spring = new AnnotationConfigApplicationContext()

  def scan(packages:String):Unit = {
    spring.scan(packages)
  }

  override def injectable(jsr303:Class[_]):Unit = {
    spring.register(jsr303)

    if (jsr303.isInstanceOf[Lifecycle]) {
      registerLifecycle(spring.getBean(jsr303).asInstanceOf[Lifecycle])
    }
  }

  override def instance[T >: K, K](clazz: Class[K]): T = spring.getBean(clazz)
}