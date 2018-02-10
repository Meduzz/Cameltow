package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object Todo {
  def apply() = Response(501)
  def text(text:String) = Response(501, body = Some(Text(text)))
  def json(json:String) = Response(501, body = Some(Json(json)))
  def html(html:String) = Response(501, body = Some(Html(html)))
  def text(bytes:Array[Byte]) = Response(501, body = Some(TextBytes(bytes)))
  def json(bytes:Array[Byte]) = Response(501, body = Some(JsonBytes(bytes)))
  def html(bytes:Array[Byte]) = Response(501, body = Some(HtmlBytes(bytes)))
}
