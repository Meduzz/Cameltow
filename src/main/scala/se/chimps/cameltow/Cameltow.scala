package se.chimps.cameltow

import io.undertow.Undertow
import io.undertow.Undertow.{Builder => UndertowBuilder}
import io.undertow.server.HttpHandler
import io.undertow.server.handlers.ResponseCodeHandler
import org.slf4j.{Logger, LoggerFactory}
import se.chimps.cameltow.framework.feaures.{GracefulShutdown, ParseForms, RequestLogging}
import se.chimps.cameltow.framework.handlers.VirtualHosts
import se.chimps.cameltow.framework.routes.{Routes, RoutingImpl}
import se.chimps.cameltow.framework.{Feature, Handler}

object Cameltow {
  def routes():Routes = new RoutingImpl()
  def virtualHosts():VirtualHosts = VirtualHosts()
  def defaults(appName:String = "default"):Builder = {
    val defaults = Map("GracefulShutdown" -> GracefulShutdown(), "RequestLogging" -> RequestLogging(appName), "ParseForms" -> ParseForms())
    new Cameltow(defaults, appName)
  }
  def blank(appName:String = "default"):Builder = new Cameltow(Map(), appName)
}

class Cameltow(private var features:Map[String, Feature], val name:String) extends Builder {
  val log:Logger = LoggerFactory.getLogger(s"$name.cameltow")

  private var handler:Option[Handler] = None

  override def handler(handler: Handler): Builder = {
    this.handler = Some(handler)
    this
  }

  override def listen(port: Int, host: String): Undertow = {
    log.info("-----")
    log.info("Enabled modules [{}]", features.keys.mkString(", "))
    log.info(s"Server listening on $host:$port.")
    Undertow.builder()
      .setHandler(featuresAsHandler)
      .addHttpListener(port, host)
      .build()
  }

  override def activate(feature: Feature): Builder = {
    features = features ++ Map(feature.getClass.getSimpleName -> feature)
    this
  }

  private def featuresAsHandler:HttpHandler = {
    val root = handler.map(_.httpHandler).getOrElse(ResponseCodeHandler.HANDLE_404)
    features.values.foldRight(root)((a,b) => {
      a.setNext(b)
      a.httpHandler
    })
  }
}

trait Builder {
  def handler(handler:Handler):Builder
  def listen(port:Int = 8080, host:String = "0.0.0.0"):Undertow
  def activate(feature:Feature):Builder
}