package se.chimps.cameltow.modules

import se.chimps.cameltow.Cameltow
import io.undertow.server.handlers.{PathTemplateHandler, PathHandler}

/**
 * Created by meduzz on 18/05/14.
 * Allows for use of "plain old undertow handlers".
 * NOTE:
 * PathTemplateHandler are mounted to / on PathHandler
 * PathHandler will atm be wrapped by:
 *  1. CanonicalPathHandler
 *  2. HttpContinueReadHandler
 *  3. EagerFormParsingHandler
 *
 * TODO how does this code style play with DI trait?
 */
trait Undertow { self:Cameltow =>

  def withPathHandlers(func:(PathHandler)=>Unit) = {
    func(self.rootHandler)
  }

  def withPathTemplateHandler(func:(PathTemplateHandler)=>Unit) = {
    func(self.pathTemplateHandler)
  }

}
