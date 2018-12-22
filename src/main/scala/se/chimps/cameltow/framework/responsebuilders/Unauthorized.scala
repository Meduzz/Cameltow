package se.chimps.cameltow.framework.responsebuilders

import se.chimps.cameltow.framework._

object Unauthorized {
    def apply(realm:String) = Response(401)
      .withHeader("WWW-Authenticate", s"""Basic realm="$realm"""")

    def text(text:String, realm:String) = Response(401, body = Some(Text(text)))
      .withHeader("WWW-Authenticate", s"""Basic realm="$realm"""")

    def json(json:String, realm:String) = Response(401, body = Some(Json(json)))
      .withHeader("WWW-Authenticate", s"""Basic realm="$realm"""")

    def html(html:String, realm:String) = Response(401, body = Some(Html(html)))
      .withHeader("WWW-Authenticate", s"""Basic realm="$realm"""")

    def text(bytes:Array[Byte], realm:String) = Response(401, body = Some(TextBytes(bytes)))
      .withHeader("WWW-Authenticate", s"""Basic realm="$realm"""")

    def json(bytes:Array[Byte], realm:String) = Response(401, body = Some(JsonBytes(bytes)))
      .withHeader("WWW-Authenticate", s"""Basic realm="$realm"""")

    def html(bytes:Array[Byte], realm:String) = Response(401, body = Some(HtmlBytes(bytes)))
      .withHeader("WWW-Authenticate", s"""Basic realm="$realm"""")
}
