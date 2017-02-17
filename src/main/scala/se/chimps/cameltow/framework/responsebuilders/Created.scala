package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework.{Html, Json, Response, Text}

object Created {
  def apply() = Response(201)
  def text(text:String) = Response(201, body = Some(Text(text)))
  def json(json:String) = Response(201, body = Some(Json(json)))
  def html(html:String) = Response(201, body = Some(Html(html)))
}
