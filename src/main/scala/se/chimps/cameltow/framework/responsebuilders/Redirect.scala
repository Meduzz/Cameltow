package se.chimps.cameltow.framework.responsebuilders

import io.undertow.util.Headers
import se.chimps.cameltow.framework.Response

object Redirect {
  def apply(url:String) = Response(301, headers = Map[String, String](Headers.LOCATION_STRING -> url))
}
