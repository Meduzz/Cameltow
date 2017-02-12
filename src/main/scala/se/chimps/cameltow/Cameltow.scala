package se.chimps.cameltow

object Cameltow {
}

class Cameltow {
}

/*
  I want to have both that implementing a single method are enought, and the ability to go all
  in with routes incl route groups like express & gin.

  val routes = Cameltow.routes()

  routes.get(/, (request) => response)
  rotes.get(/js, Resource(/assets/js)) // funky shorthand solution to a semi complex problem
  val group = routes.group(/group) // translates into a PathHandler with a child PathTemplateHandler
  group.get(/, (request) => response)

  With the handler method defined as request => response, makes writing middleware/filters as easy
  as composing in a method in front or behind the handler.

  val server = Cameltow.listen(port, host = 0.0.0.0)
  server.shutdown()

  // TBD start.
  server.activate(WS)
  server.activate(HTTP2)
  server.activate(Undertow.featureX)
  server.activate(Undertow.100accept(max-size) // <- must generate a predicate, which are sort of painful.
  // TBD end.

  val req = Request(body:Option[Body]) // <- Unapply
  val pathParam = req.path(name) // might be merged with query due to (PathTemplateHandler.rewriteQueryParameters)
  val cookieParam = req.cookie(name):Seq[String]
  val queryParam = req.query(name):Seq[String]
  val headerParam = req.header(name) // Create an object with the most common & funky header-names.

  trait Body
  Form(kv:Map[String, Seq[String]]) extends Body
  File(file:File) extends Body
  Encoded(bytes:Array[Byte]) extends Body
 */