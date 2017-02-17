package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework.{Html, Json, Response, Text}

object Forbidden {
  def apply() = Response(403)
  def text(text:String) = Response(403, body = Some(Text(text)))
  def json(json:String) = Response(403, body = Some(Json(json)))
  def html(html:String) = Response(403, body = Some(Html(html)))
}
