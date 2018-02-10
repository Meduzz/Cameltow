package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object Error {
  def apply() = Response(500)
  def apply(body:ResponseBody[_]) = Response(500, body = Some(body))
  def text(text:String) = Response(500, body = Some(Text(text)))
  def html(html:String) = Response(500, body = Some(Html(html)))
  def json(json:String) = Response(500, body = Some(Json(json)))
  def text(bytes:Array[Byte]) = Response(500, body = Some(TextBytes(bytes)))
  def json(bytes:Array[Byte]) = Response(500, body = Some(JsonBytes(bytes)))
  def html(bytes:Array[Byte]) = Response(500, body = Some(HtmlBytes(bytes)))
}
