package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object Conflict {
  def apply() = Response(409)
  def apply(body:ResponseBody[_]) = Response(409, body = Some(body))
  def text(text:String) = Response(409, body = Some(Text(text)))
  def json(json:String) = Response(409, body = Some(Json(json)))
  def html(html:String) = Response(409, body = Some(Html(html)))
  def text(bytes:Array[Byte]) = Response(409, body = Some(TextBytes(bytes)))
  def json(bytes:Array[Byte]) = Response(409, body = Some(JsonBytes(bytes)))
  def html(bytes:Array[Byte]) = Response(409, body = Some(HtmlBytes(bytes)))
}
