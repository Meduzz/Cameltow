package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object NotFound {
  def apply() = Response(404)
  def apply(body:ResponseBody[_]) = Response(404, body = Some(body))
  def text(text:String) = Response(404, body = Some(Text(text)))
  def json(json:String) = Response(404, body = Some(Json(json)))
  def html(html:String) = Response(404, body = Some(Html(html)))
}
