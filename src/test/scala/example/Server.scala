package example

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework.feaures.Error
import se.chimps.cameltow.framework.handlers.Methods._
import se.chimps.cameltow.framework.handlers.{Action, Static}
import se.chimps.cameltow.framework.responsebuilders.{BadRequest, Ok, Error => FiveHundred}
import se.chimps.cameltow.framework.{Form, FormItem, Response}

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

    Ok.html(body.toString())
  }))
  routes.template("/p/{word}", Action.sync(req => {
    val word = req.query("word").getOrElse("MISSING")
    Ok.text(word)
  }))
  routes.prefix("/static", GET(Static.classpath(listDirectory = true)))
  routes.exact("/error", Action(req => Future.failed(new RuntimeException("Dying."))))
  routes.exact("/json", POST(Action.sync(req => {
    req.body match {
      case Form(data) => {
        data("text").head match {
          // TODO åäö are garbled.
          case FormItem(value) => Ok.json(s"""{"text":"$value"}""")
          case _ => BadRequest.text("I dont understand.")
        }
      }
      case any => BadRequest.text("fix your content-type.")
    }
  })))

  val server = Cameltow.defaults()
    .activate(Error(errorHandling))
    .handler(routes.handler)
    .listen()

  server.start()

  def errorHandling:PartialFunction[Throwable, Response] = {
    case e:Throwable => FiveHundred.text(e.getMessage)
  }
}