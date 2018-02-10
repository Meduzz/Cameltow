package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object BadRequest {
  def apply() = Response(400)
  def apply(body:ResponseBody[_]) = Response(400, body = Some(body))
  def text(text:String) = Response(400, body = Some(Text(text)))
  def json(json:String) = Response(400, body = Some(Json(json)))
  def html(html:String) = Response(400, body = Some(Html(html)))
  def text(bytes:Array[Byte]) = Response(400, body = Some(TextBytes(bytes)))
  def json(bytes:Array[Byte]) = Response(400, body = Some(JsonBytes(bytes)))
  def html(bytes:Array[Byte]) = Response(400, body = Some(HtmlBytes(bytes)))
}
