package se.chimps.cameltow.framework

import io.undertow.io.Receiver.{FullBytesCallback, PartialBytesCallback}
import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.form.FormDataParser

import scala.collection.JavaConverters._
import scala.collection.mutable.Queue
import scala.concurrent.Promise

object Request {
  def apply(context:HttpServerExchange):Request = new Request(context)
}

// TODO hide the exchange?
class Request(val exchange:HttpServerExchange) {
  def method:String = exchange.getRequestMethod.toString
  def header(name:String):Seq[String] = exchange.getRequestHeaders.get(name).asScala
  def query(name:String):Seq[String] = exchange.getQueryParameters.get(name).asScala.toSeq
  def cookie(name:String):Cookie = new Cookie(exchange.getRequestCookies.get(name))
  def body:Body = {
    if (exchange.getAttachment(FormDataParser.FORM_DATA) != null) {
      // Form or File
      val data = exchange.getAttachment(FormDataParser.FORM_DATA)
      val it = data.iterator().asScala

      val formItems = it.map(key => {
        data.get(key).asScala.map(item => {
          if (item.isFile) {
            // It's a file...
            FileItem(item.getPath)
          } else {
            // It's a form (key=value)
            FormItem(item.getValue)
          }
        }).toSeq
      })
      Form(it.zip(formItems).toMap)
    } else if (exchange.getRequestHeaders.get("Content-Encoding").contains("chunked")) {
      // Stream?
      val queue = Queue[Array[Byte]]()
      exchange.getRequestReceiver.receivePartialBytes(new PartialBytesCallback {
        var size = 1
        override def handle(exchange: HttpServerExchange, message: Array[Byte], last: Boolean) = {
          queue.enqueue(message)
          if (!last) {
            size = size + 1
            queue.sizeHint(size)
          } else {
            queue.enqueue(Array[Byte]())
          }
        }
      })
      Stream(queue)
    } else {
      val promise = Promise[Array[Byte]]()
      exchange.getRequestReceiver.receiveFullBytes(new FullBytesCallback {
        override def handle(exchange: HttpServerExchange, message: Array[Byte]) = promise.success(message)
      })
      Encoded(promise.future)
    }
  }
}

