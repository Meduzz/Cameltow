package se.chimps.cameltow.framework.feaures

import io.undertow.server.ExchangeCompletionListener.NextListener
import io.undertow.server.{ExchangeCompletionListener, HttpHandler, HttpServerExchange}
import org.slf4j.LoggerFactory
import se.chimps.cameltow.framework.Feature

import scala.math.BigDecimal.RoundingMode

object RequestLogging {
  def apply() = new RequestLogging
}

class RequestLogging extends Feature {
  private val log = LoggerFactory.getLogger("cameltow.request")

  private var next:HttpHandler = _

  override def httpHandler: HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange):Unit = {
      val start = System.nanoTime()
      val method = exchange.getRequestMethod.toString
      val url = exchange.getRequestPath

      exchange.addExchangeCompleteListener(new ExchangeCompletionListener {
        override def exchangeEvent(exchange: HttpServerExchange, nextListener: NextListener):Unit = {
          val end = System.nanoTime()
          val code = exchange.getStatusCode
          val timeRaw = BigDecimal(end - start)

          val time = (timeRaw / 1000000).setScale(2, RoundingMode.HALF_UP)

          log.info(s"$method $url [$code] ($time ms)")
          nextListener.proceed()
        }
      })

      next.handleRequest(exchange)
    }
  }

  override def setNext(httpHandler: HttpHandler): Unit = this.next = httpHandler
}