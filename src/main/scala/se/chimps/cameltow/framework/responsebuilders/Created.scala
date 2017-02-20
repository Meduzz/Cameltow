package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object Created {
  def apply() = Response(201)
  def apply(body:ResponseBody[_]) = Response(201, body = Some(body))
  def text(text:String) = Response(201, body = Some(Text(text)))
  def json(json:String) = Response(201, body = Some(Json(json)))
  def html(html:String) = Response(201, body = Some(Html(html)))
}
