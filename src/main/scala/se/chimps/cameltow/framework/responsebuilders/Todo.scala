package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework.{Html, Json, Response, Text}

object Todo {
  def apply() = Response(501)
  def text(text:String) = Response(501, body = Some(Text(text)))
  def json(json:String) = Response(501, body = Some(Json(json)))
  def html(html:String) = Response(501, body = Some(Html(html)))
}
