Cameltow 2.0
========

Put scala makeup on the Undertow webserver. 
Going from a multifeature framework (v1.0) to a bit of a 
single feature anti framework (v2.0).

## Hello world

```
  import se.chimps.cameltow.Cameltow
  import se.chimps.cameltow.framework.handlers.Action
  import se.chimps.cameltow.framework.responsebuilders.Ok

  val server = Cameltow.defaults()
    .handler(Action(request => Ok.text("Hello world!"))
    .listen()

  server.start()
```

Should create a server that binds to 0.0.0.0:8080 and answers
"Hello world!" alot.

That's not very useful though, notice the use of ```Cameltow.routes()```
in this next example.

```
  val routes = Cameltow.routes()
  
  routes.GET("/:word", Action.sync(req => {
    val word = req.pathParam("word")
    Ok.text(word)
  })
  
  val server = Cameltow.defaults()
    .handler(routes.handler)
    .listen()

  server.start()
```

## Handlers

In Undertow, chains of handlers are built to 
handle http requests. In Cameltow, Handlers are 
at the edge of that very pipe. 
A handler have the following definition. 

```
trait Handler {
  def httpHandler:HttpHandler
}
```

This rather week definition both matches 
the Undertow HttpHandler and buys us alot
of flexibility.

TODO link to handlers package.

## Features

In Cameltow there's another type of handlers 
that is called features. Under the hood, these too 
are handlers. But it does not make sense to add 
these to each route manually, but rather something 
you'd enable before you start defining your routes.
Features are defined like this:

```
trait Feature {
  def httpHandler:HttpHandler
  def setNext(httpHandler: HttpHandler)
}
```

By starting Cameltow with ```Cameltow.defaults()```
a couple of nice to have features have been enabled
out of the box for you:

* GracefulShutdown, that will drain requests before
shutting down.
* RequestLogging, a logger that will (in INFO) log 
the method, the path the response code and how long
time was spent handling the request, in ms.
* ParseForms, a handler that parses form data, into
a more managable form.

And there's more to enable, when/if needed.

TODO add link to features.

## ResponseBuilders

Response builders are the last bit of the puzzle. 
The response case class are not a bad way to end
a request. But, I belive that the response builders
simply are such a much better tool. And by the end
of the day, they still generate a Response.

Generally response builders dont have a common
trait to inherit from. But they pretty much
share the same methods/logic. Like the Ok builder:

```
object Ok {
  def apply():Response = Response(200)
  def apply(body:ResponseBody[_]) = Response(200, body = Some(body))
  def text(text:String) = Response(200, body = Some(Text(text)))
  def json(json:String) = Response(200, body = Some(Json(json)))
  def html(html:String) = Response(200, body = Some(Html(html)))
}
```