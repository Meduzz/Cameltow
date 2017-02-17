package example

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework.feaures.Error
import se.chimps.cameltow.framework.handlers.Methods._
import se.chimps.cameltow.framework.handlers.{Action, Static}
import se.chimps.cameltow.framework.{Html, Response, Text}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Server extends App {

  val routes = Cameltow.routes()

  routes.exact("/", GET(Action.sync { req =>
    val body = <html>
      <head>
        <title>Hello!</title>
      </head>
      <body>
        <h1>Hello!</h1>
        <div>
          <a href="/static/form.html">Form</a>
        </div>
      </body>
    </html>

    Response(200, body = Some(Html(body.toString())))
  }))
  routes.template("/p/{word}", Action.sync(req => {
    val word = req.query("word").getOrElse("MISSING")
    Response(200, body = Some(Text(word)))
  }))
  routes.prefix("/static", GET(Static.classpath(listDirectory = true)))
  routes.exact("/error", Action(req => Future.failed(new RuntimeException("Dying."))))

  val server = Cameltow.defaults()
    .activate(Error(errorHandling))
    .handler(routes.handler)
    .listen()

  server.start()

  def errorHandling:PartialFunction[Throwable, Response] = {
    case e:Throwable => Response(500, body = Some(Text(e.getMessage)))
  }
}