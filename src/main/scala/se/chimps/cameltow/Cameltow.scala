package se.chimps.cameltow

import se.chimps.cameltow.framework.{SpritsWrapper, RouteHolder}
import se.chimps.cameltow.logging.Logging
import io.undertow.Undertow
import io.undertow.server.handlers._
import scala.collection.mutable
import io.undertow.server.handlers.form.EagerFormParsingHandler
import se.chimps.cameltow.framework.lifecycle.Lifecycle
import io.undertow.server.handlers.resource.{FileResourceManager, ResourceHandler}
import java.io.File
import io.undertow.predicate.Predicates

/**
 * Created by meduzz on 22/04/14.
 * TODO break out all "modules" into modules :)
 */
abstract class Cameltow extends App with Logging {
  // TODO implement a config... again
  val builder:Undertow.Builder = Undertow.builder()
  var server:Undertow = null

  // TODO move framework specific code out of here.
  val pathHandler:PathHandler = new PathHandler()
  val pathTemplateHandler:PathTemplateHandler = new PathTemplateHandler()
  val formHandler:EagerFormParsingHandler = new EagerFormParsingHandler()

  val lifecycle:mutable.Seq[Lifecycle] = mutable.Seq()

  def initialize()

  if (logger.isDebugEnabled) {
    logger.debug("Starting Cameltow with these parameters: {}.", args)
  } else {
    logger.info("Starting Cameltow.")
  }

  initialize()

  def listen(port:Int):Unit = {
    listen("127.0.0.1", port)
  }

  def listen(host:String, port:Int):Unit = {
    // TODO add support for websocket handlers in some neat way.
    pathHandler.addPrefixPath("/", pathTemplateHandler)

    RouteHolder.iterator.foreach { key =>
      if (key.contains("{")) {
        pathTemplateHandler.add(key, new SpritsWrapper(RouteHolder.get(key)))
      } else {
        pathHandler.addExactPath(key, new SpritsWrapper(RouteHolder.get(key)))
      }
    }

    lifecycle.foreach { lc =>
      lc.start()
    }

    server = builder.setHandler(new CanonicalPathHandler(new HttpContinueReadHandler(formHandler.setNext(pathHandler)))).addHttpListener(port, host).build()

    server.start()
  }

  def staticContent(baseUrl:String, path:File):Unit = {
    logger.info(s"Mounting static files from ${path.getAbsolutePath} to ${baseUrl}")
    // TODO this is not working....
    val resourceHandler = new ResourceHandler(new FileResourceManager(path, 1l))
    resourceHandler.setDirectoryListingEnabled(true)
    resourceHandler.setAllowed(Predicates.truePredicate())
    resourceHandler.setCachable(Predicates.falsePredicate())

    pathHandler.addPrefixPath(baseUrl, resourceHandler)
  }

  def stop():Unit = {
    lifecycle.foreach { lc =>
      lc.stop()
    }

    server.stop()
  }
}