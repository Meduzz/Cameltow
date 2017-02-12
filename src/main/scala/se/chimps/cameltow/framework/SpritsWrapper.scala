package se.chimps.cameltow.framework

import java.nio.ByteBuffer

import io.undertow.server.handlers.form.FormDataParser
import io.undertow.server.{HttpHandler, HttpServerExchange}
import se.chimps.cameltow.framework.Http.Method
import se.chimps.cameltow.framework.ResponseBuilders.NotFound
import se.chimps.cameltow.framework.undertow.{HeaderMapWrapper, RequestParameterWrapper}

/**
 * Created by meduzz on 14/05/14.
 */
private[cameltow] class SpritsWrapper(val routes:Map[Method, BasicAction]) extends HttpHandler {
  override def handleRequest(exchange: HttpServerExchange): Unit = {
    val req = BuildSpritsRequest(exchange)

    val res:Response = routes.get(req.method) match {
      case None => NotFound() // Lets 404 this one.
      case Some(action:BasicAction) => action(req) // Execute action.
    }

    exchange.setResponseCode(res.statusCode)
    var resHead = HeaderMapWrapper(exchange.getResponseHeaders)
    res.headers.foreach({ kv =>
      resHead = resHead + (kv._1 -> kv._2)
    })
    res.body match {
      case Some(body:Array[Byte]) => exchange.getResponseSender.send(ByteBuffer.wrap(body))
      case None => exchange.getResponseSender.send(ByteBuffer.allocate(0))
    }
  }
}

private[cameltow] object BuildSpritsRequest {
  def apply(exchange:HttpServerExchange):Request = {
    import Http._

    val method = Method(exchange.getRequestMethod.toString)
    val headers = HeaderMapWrapper(exchange.getRequestHeaders)

    val buffer = method match {
      case Methods.POST|Methods.PUT => {
        if (exchange.getAttachment(FormDataParser.FORM_DATA) == null) {
          val len = headers("Content-Length") match {
            case s: String => s.toInt
            case _ => 1024 * 1024 // TODO make a config var?
          }
          val channel = exchange.getRequestChannel
          val buf = ByteBuffer.allocate(len)
          channel.read(buf)
          buf
        } else {
          ByteBuffer.allocate(0)
        }
      }
      case _ => ByteBuffer.allocate(0)
    }

    var queryParams = RequestParameterWrapper(exchange.getQueryParameters)

    val formParams = exchange.getAttachment(FormDataParser.FORM_DATA)
    if (formParams != null) {
      val it = formParams.iterator()

      while (it.hasNext) {
        val key = it.next()
        queryParams = queryParams + (key -> formParams.getFirst(key).getValue)
      }
    }

    new Request(method, headers, queryParams, if (buffer.remaining() > 0) { Some(buffer.array()) } else { None })
  }
}