package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework.{Html, Json, Response, Text}

object Error {
  def apply() = Response(500)
  def text(text:String) = Response(500, body = Some(Text(text)))
  def html(html:String) = Response(500, body = Some(Html(html)))
  def json(json:String) = Response(500, body = Some(Json(json)))
}
