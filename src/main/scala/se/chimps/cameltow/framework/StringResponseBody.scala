package se.chimps.cameltow.framework

trait ResponseBody[T] {
  def contentType:Option[String]
  def apply():T
}
trait StringResponseBody extends ResponseBody[String] {
  def contentType:Option[String]
}
trait ByteResponseBody extends ResponseBody[Array[Byte]] {
  def contentType:Option[String]
}
case class Text(content:String, encoding:String = "utf-8") extends StringResponseBody {
  override def contentType: Option[String] = Some(s"text/plain; charset=$encoding")

  override def apply(): String = content
}
case class Html(content:String, encoding:String = "utf-8") extends StringResponseBody {
  override def contentType: Option[String] = Some(s"text/html; charset=$encoding")

  override def apply(): String = content
}
case class Json(content:String, encoding:String = "utf-8") extends StringResponseBody {
  override def contentType: Option[String] = Some(s"application/json; charset=$encoding")

  override def apply(): String = content
}
case class TextBytes(content:Array[Byte], encoding:String = "utf-8") extends ByteResponseBody {
  override def contentType: Option[String] = Some(s"text/plain; charset=$encoding")

  override def apply(): Array[Byte] = content
}
case class HtmlBytes(content:Array[Byte], encoding:String = "utf-8") extends ByteResponseBody {
  override def contentType: Option[String] = Some(s"text/html; charset=$encoding")

  override def apply(): Array[Byte] = content
}
case class JsonBytes(content:Array[Byte], encoding:String = "utf-8") extends ByteResponseBody {
  override def contentType: Option[String] = Some(s"application/json; charset=$encoding")

  override def apply(): Array[Byte] = content
}
case class BinaryBytes(content:Array[Byte], encoding:String = "") extends ByteResponseBody {
  override def contentType: Option[String] = Some("application/octet-stream")

  override def apply(): Array[Byte] = content
}
