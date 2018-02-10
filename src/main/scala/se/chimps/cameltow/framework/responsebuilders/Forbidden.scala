package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object Forbidden {
  def apply() = Response(403)
  def text(text:String) = Response(403, body = Some(Text(text)))
  def json(json:String) = Response(403, body = Some(Json(json)))
  def html(html:String) = Response(403, body = Some(Html(html)))
  def text(bytes:Array[Byte]) = Response(403, body = Some(TextBytes(bytes)))
  def json(bytes:Array[Byte]) = Response(403, body = Some(JsonBytes(bytes)))
  def html(bytes:Array[Byte]) = Response(403, body = Some(HtmlBytes(bytes)))
}
