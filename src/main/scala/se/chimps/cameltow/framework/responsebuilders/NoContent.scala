package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework.Response

object NoContent {
  def apply() = Response(204)
}
