package se.chimps.cameltow.di.util

import org.springframework.context.annotation.{Bean, Configuration}
import se.chimps.cameltow.di.util.Common.{Dummy, LifecycleImpl}
import se.chimps.cameltow.lifecycle.Lifecycle

/**
 * Created by meduzz on 05/06/14.
 */
@Configuration
class SpringTestConfig {

  @Bean
  def lifecycle():Lifecycle = {
    new LifecycleImpl
  }

  @Bean
  def dummy():Dummy = {
    new Dummy(lifecycle())
  }

}
