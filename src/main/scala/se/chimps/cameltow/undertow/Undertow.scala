package se.chimps.cameltow.undertow

import io.undertow.server.HttpHandler

/**
 * Created by meduzz on 18/05/14.
 */
trait Undertow {

  // TODO implement on top of RoutingHandler once it gets available in Maven.
  def withHandlers(func:()=>HttpHandler) = {
    // TBD
  }
}
