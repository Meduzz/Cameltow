package example

import java.util.concurrent.TimeUnit

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework.feaures.{Error, RequestLogging}
import se.chimps.cameltow.framework.handlers.{Action, Static}
import se.chimps.cameltow.framework.responsebuilders.{BadRequest, Ok, Error => FiveHundred}
import se.chimps.cameltow.framework.{Form, FormItem, Response}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

object Server extends App {

  implicit val timeout = Duration(1L, TimeUnit.SECONDS)

  val routes = Cameltow.routes()

  // TODO åäö are garbled
  routes.GET("/", Action.sync { req =>
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
  })

  routes.GET("/p/:dir/:file(.*[.js|.css]+)", Action.sync(req => {
    val dir = req.pathParam("dir")
    val file = req.pathParam("file")
    Ok.text(s"Looking for $file in $dir.")
  }))

  // my regex are not strong enough to wire these two in one go.
  routes.GET("/static/", Static.classpath(listDirectory = true))
  routes.GET("/static/:file(.*)", Static.classpath())

  routes.GET("/error", Action(req => Future.failed(new RuntimeException("Dying."))))

  routes.POST("/json", Action.sync(req => {
    req.body match {
      case Form(data) => {
        data("text").head match {
          // TODO åäö are garbled.
          case FormItem(value) => Ok.json(s"""{"text":"$value"}""")
          case _ => BadRequest.text("I dont understand.")
        }
      }
      case any => BadRequest.text("Fix your content-type.")
    }
  }))

  val server = Cameltow.defaults()
    .activate(Error(errorHandling))
    .activate(RequestLogging())
    .handler(routes.handler)
    .listen()

  server.start()

  def errorHandling:PartialFunction[Throwable, Response] = {
    case e:Throwable => FiveHundred.text(e.getMessage)
  }
}