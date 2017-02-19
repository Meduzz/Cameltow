package se.chimps.cameltow

import io.undertow.Undertow
import io.undertow.server.HttpHandler
import io.undertow.server.handlers.ResponseCodeHandler
import se.chimps.cameltow.framework.feaures.{GracefulShutdown, Gzip, ParseForms}
import se.chimps.cameltow.framework.routes.{Routes, RoutingImpl}
import se.chimps.cameltow.framework.{Feature, Handler}

object Cameltow {
  def routes():Routes = new RoutingImpl
  def defaults():Builder = {
    val defaults = Map("GracefulShutdown" -> GracefulShutdown(), "Gzip" -> Gzip(), "ParseForms" -> ParseForms())
    new Cameltow(defaults)
  }
  def blank():Builder = new Cameltow(Map())
}

class Cameltow(private var features:Map[String, Feature]) extends Builder {
  private var handler:Option[Handler] = None

  override def handler(handler: Handler): Builder = {
    this.handler = Some(handler)
    this
  }

  override def listen(port: Int, host: String): Undertow = {
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
    features.values.foldLeft(root)((a,b) => {
      b.setNext(a)
      b.httpHandler
    })
  }
}

trait Builder {
  def handler(handler:Handler):Builder
  def listen(port:Int = 8080, host:String = "0.0.0.0"):Undertow
  def activate(feature:Feature):Builder
}

/*
  I want to have both that implementing a single method are enough, and the ability to go all
  in with routes incl route groups like express & gin.

  val routes = Cameltow.routes()

  routes.get(/, Handler)
  routes.get(/static/, Static.dir|file|classpath(/assets)) // funky shorthand solution to a semi complex problem
  routes.get(/secret,Authorize(delegate)(Cached(Action))) // this type of chaining would be awesome!.. but also awfully close to feature creep.

  Action((Request)=>Future[Response])

  With the action defined as request => Future[response], writing middleware/filters are as easy
  as composing methods.

  val server = Cameltow.listen(port, host = 0.0.0.0)
  server.stop()

  Cameltow.activate(WS)
  Cameltow.activate(HTTP2)
  Cameltow.activate(Undertow.featureX)
  Cameltow.activate(Undertow.100accept(max-size) // <- must generate a predicate, which are sort of painful.

  val req = Request()
  val pathParam = req.path(name) // might be merged with query due to (PathTemplateHandler.rewriteQueryParameters)
  val cookieParam = req.cookie(name):Seq[String]
  val queryParam = req.query(name):Seq[String]
  val headerParam = req.header(name)
  val body = req.body

  trait RequestBody
  Form(kv:Map[String, Seq[String]]) extends Body
  Encoded(bytes:Array[Byte]) extends Body
  Stream(chunks:Queue[Array[Byte]]) extends Body

  // TODO Create an object with the most common & funky header-names.
  // TODO Create a feature that sort out encoding issues (make things utf-8)
  // TODO Are we only using IoThreads? Could be an automatic Feature style solution to this?
  // TODO add an apply(ResponseBody) method to all response builders that takes a body.
  // TODO add feature for requestlogging, nothing verbose, only the common stuff.
 */