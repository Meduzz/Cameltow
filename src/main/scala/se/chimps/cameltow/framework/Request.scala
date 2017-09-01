package se.chimps.cameltow.framework

import io.undertow.io.Receiver.{FullBytesCallback, PartialBytesCallback}
import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.form.FormDataParser

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.Promise

object Request {
  def apply(context:HttpServerExchange):Request = new Request(context)
}

class Request(val exchange:HttpServerExchange) {
  def method:String = exchange.getRequestMethod.toString
  def pathParam(name:String):String = {
    val params = exchange.getPathParameters.get(name)

    if (params.size() > 0) {
      params.getFirst
    } else {
      null
    }
  }
  def charset:String = exchange.getRequestCharset
  def header(name:String):Option[String] = headerAll(name).headOption
  def cookie(name:String):Option[String] = {
    if (exchange.getRequestCookies != null && exchange.getRequestCookies.containsKey(name)) {
      Some(exchange.getRequestCookies.get(name).getValue)
    } else {
      None
    }
  }
  def query(name:String):Option[String] = queryAll(name).headOption
  def queryAll(name:String):Seq[String] = {
    if (exchange.getQueryParameters.containsKey(name)) {
      exchange.getQueryParameters.get(name).asScala.toSeq
    } else {
      Seq()
    }
  }
  def headerAll(name:String):Seq[String] = {
    if (exchange.getRequestHeaders.contains(name)) {
      exchange.getRequestHeaders.get(name).asScala
    } else {
      Seq()
    }
  }
  def body:RequestBody = {
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
      Form(data.iterator().asScala.zip(formItems).toMap)
    } else if (exchange.getRequestHeaders.contains("Content-Encoding") && exchange.getRequestHeaders.get("Content-Encoding").contains("chunked")) {
      // Stream?
      val queue = mutable.Queue[Array[Byte]]()
      exchange.getRequestReceiver.receivePartialBytes(new PartialBytesCallback {
        var size = 1
        override def handle(exchange: HttpServerExchange, message: Array[Byte], last: Boolean):Unit = {
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
        override def handle(exchange: HttpServerExchange, message: Array[Byte]):Unit = promise.success(message)
      })
      Encoded(promise.future)
    }
  }
}

