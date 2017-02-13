package se.chimps.cameltow

object Cameltow {
}

class Cameltow {
}

/*
  I want to have both that implementing a single method are enough, and the ability to go all
  in with routes incl route groups like express & gin.

  val routes = Cameltow.routes()

  routes.get(/, Handler)
  rotes.get(/static/, Static|StaticClasspath.dir(/assets)) // funky shorthand solution to a semi complex problem
  val group = routes.group(/group) // translates into a RoutingHandler
  group.get(/, Handler)

  Action((Request)=>Future[Response])

  With the action defined as request => Future[response], writing middleware/filters are as easy
  as composing methods.

  val server = Cameltow.listen(port, host = 0.0.0.0)
  server.shutdown()

  // TBD start.
  Cameltow.activate(WS)
  Cameltow.activate(HTTP2)
  Cameltow.activate(Undertow.featureX)
  Cameltow.activate(Undertow.100accept(max-size) // <- must generate a predicate, which are sort of painful.
  // TBD end.

  val req = Request(body:Option[Body])
  val pathParam = req.path(name) // might be merged with query due to (PathTemplateHandler.rewriteQueryParameters)
  val cookieParam = req.cookie(name):Seq[String]
  val queryParam = req.query(name):Seq[String]
  val headerParam = req.header(name)

  // TODO Create an object with the most common & funky header-names.
  // TODO Create a trait with ResponseBuilders.

  trait Body
  Form(kv:Map[String, Seq[String]]) extends Body
  Encoded(bytes:Array[Byte]) extends Body
  Stream(chunks:Queue[Array[Byte]]) extends Body
 */