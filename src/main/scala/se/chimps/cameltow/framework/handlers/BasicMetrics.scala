package se.chimps.cameltow.framework.handlers

import io.undertow.server.HttpHandler
import io.undertow.server.handlers.MetricsHandler
import io.undertow.server.handlers.MetricsHandler.MetricResult
import se.chimps.cameltow.framework.Handler

/*
  Until routing groups have materialized, this handler will be pretty irrelevant.
 */
object BasicMetrics {
  private var metricsDelegate = new MetricsDelegate

  def delegate():MetricsDelegate = metricsDelegate

  def apply(name:String, handler:Handler):Handler = {
    val metricsHandler = new BasicMetrics(handler)
    metricsDelegate(name, metricsHandler)
  }
}

class MetricsDelegate {
  private var metricData = Map[String, BasicMetrics]()

  def apply(name:String, handler:BasicMetrics):Handler = {
    metricData = metricData ++ Map(name -> handler)
    handler
  }

  def metrics():Map[String, Metric] = {
    metricData.flatMap(kv => {
      val (name, metrix) = kv
      val data = metrix.metrics()
      Map(name -> Metric(data.getMaxRequestTime, data.getMinRequestTime, data.getTotalRequestTime, data.getTotalRequests))
    })
  }

  def reset():Unit = metricData.values.foreach(_.reset())
}

class BasicMetrics(val next:Handler) extends Handler {
  val metricsHandler = new MetricsHandler(next.httpHandler)

  override def httpHandler: HttpHandler = metricsHandler

  def metrics():MetricResult = metricsHandler.getMetrics
  def reset():Unit = metricsHandler.reset()
}

case class Metric(max:Int, min:Int, total:Long, totalRequest:Long)