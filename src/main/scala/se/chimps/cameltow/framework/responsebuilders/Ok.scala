package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object Ok {
  def apply():Response = Response(200)
  def apply(body:ResponseBody[_]) = Response(200, body = Some(body))
  def text(text:String) = Response(200, body = Some(Text(text)))
  def json(json:String) = Response(200, body = Some(Json(json)))
  def html(html:String) = Response(200, body = Some(Html(html)))
}
