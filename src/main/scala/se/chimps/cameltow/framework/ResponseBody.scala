package se.chimps.cameltow.framework

trait ResponseBody[T] {
  def contentType:Option[String]
  def content:T
}
case class Text(content:String) extends ResponseBody[String] {
  override def contentType: Option[String] = Some("text/plain")
}
case class Html(content:String) extends ResponseBody[String] {
  override def contentType: Option[String] = Some("text/html")
}
case class Json(content:String) extends ResponseBody[String] {
  override def contentType: Option[String] = Some("application/json")
}