package se.chimps.cameltow.modules

import java.io.File
import io.undertow.server.handlers.resource.{FileResourceManager, ResourceHandler}
import io.undertow.predicate.Predicates
import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.lifecycle.Lifecycle
import se.chimps.cameltow.framework.{SpritsWrapper, RouteHolder, SpritsController}
import scala.collection.mutable

/**
 * Created by meduzz on 24/04/14.
 */
trait Sprits { self:Cameltow =>

  private val spritsLifecycle = new SpritsLifecycle(self)
  self.registerLifecycle(spritsLifecycle)

  private var controllers:ControllerClassRefHolder = null


  /**
   * Will initialize the controller.
   * @param controller
   */
  def registerController(controller:SpritsController) = {
    controller()
  }

  def addStaticContent(baseUrl:String, path:File) = {
    logger.info(s"Mounting static files from ${path.getAbsolutePath} to ${baseUrl}")
    spritsLifecycle.staticContent(baseUrl, path)
  }


  def registerController(clazz:Class[SpritsController]) = {
    if (!self.isInstanceOf[DependencyInjection]) {

    } else if (controllers == null) {
      controllers = new ControllerClassRefHolder(this)
      self.registerLifecycle(controllers)
    }
  }

}

private class SpritsLifecycle(val app:Cameltow) extends Lifecycle {

  def staticContent(baseUrl:String, path:File):Unit = {
    val resourceHandler = new ResourceHandler(new FileResourceManager(path, 1l))
    resourceHandler.setDirectoryListingEnabled(true)
    resourceHandler.setAllowed(Predicates.truePredicate())
    resourceHandler.setCachable(Predicates.falsePredicate())

    app.rootHandler.addPrefixPath(baseUrl, resourceHandler)
  }

  override def start():Unit = {
    // Initialize routes.
    RouteHolder.iterator.foreach { key =>
      if (key.contains("{")) {
        app.pathTemplateHandler.add(key, new SpritsWrapper(RouteHolder.get(key)))
      } else {
        app.rootHandler.addExactPath(key, new SpritsWrapper(RouteHolder.get(key)))
      }
    }
  }

  override def stop():Unit = {}
}

private class ControllerClassRefHolder(val di:Cameltow with Sprits) extends Lifecycle {
  val controllers:mutable.MutableList[Class[SpritsController]] = mutable.MutableList[Class[SpritsController]]()

  def add(controller:Class[SpritsController]) = controllers += controller

  override def start():Unit = controllers.foreach { ctrl =>
    val instance = di.asInstanceOf[DependencyInjection].instance(ctrl) // <- safe since this code wont run unless DI trait are present.
    di.registerController(instance)
  }

  override def stop():Unit = {}
}