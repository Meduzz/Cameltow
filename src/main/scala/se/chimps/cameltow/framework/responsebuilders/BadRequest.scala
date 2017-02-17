package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework.{Html, Json, Response, Text}

object BadRequest {
  def apply() = Response(400)
  def text(text:String) = Response(400, body = Some(Text(text)))
  def json(json:String) = Response(400, body = Some(Json(json)))
  def html(html:String) = Response(400, body = Some(Html(html)))
}
