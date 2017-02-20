package se.chimps.cameltow.framework

trait ResponseBody[T] {
  def contentType:Option[String]
  def content:T
}
case class Text(content:String, encoding:String = "utf-8") extends ResponseBody[String] {
  override def contentType: Option[String] = Some(s"text/plain; charset=$encoding")
}
case class Html(content:String, encoding:String = "utf-8") extends ResponseBody[String] {
  override def contentType: Option[String] = Some(s"text/html; charset=$encoding")
}
case class Json(content:String, encoding:String = "utf-8") extends ResponseBody[String] {
  override def contentType: Option[String] = Some(s"application/json; charset=$encoding")
}