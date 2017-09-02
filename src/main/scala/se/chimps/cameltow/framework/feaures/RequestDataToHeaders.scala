package se.chimps.cameltow.framework.feaures

import io.undertow.server.{HttpHandler, HttpServerExchange}
import io.undertow.util.HttpString
import se.chimps.cameltow.framework.Feature
import se.chimps.cameltow.framework.feaures.RequestDataToHeaders.DataPoint

object RequestDataToHeaders {
  type DataPoint = (HttpServerExchange) => Tuple2[String, String]

  def apply(points:Seq[DataPoint]) = new RequestDataToHeaders(points)
}

class RequestDataToHeaders(val points:Seq[DataPoint]) extends Feature {

  private var next:HttpHandler = _

  override def httpHandler: HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange): Unit = {
      points.map(_.apply(exchange)).foreach(t => {
        val (key:String, value:String) = t
        exchange.getRequestHeaders.addLast(new HttpString(key), value)
      })

      next.handleRequest(exchange)
    }
  }

  override def setNext(httpHandler: HttpHandler): Unit = next = httpHandler
}

