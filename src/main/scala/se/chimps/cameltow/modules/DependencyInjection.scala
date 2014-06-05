package se.chimps.cameltow.modules

import se.chimps.cameltow.{CameltowApp}

/**
 * Created by meduzz on 16/05/14.
 */
trait DependencyInjection { self:CameltowApp =>

  def instance[T>:K, K](clazz:Class[K]):T
}
