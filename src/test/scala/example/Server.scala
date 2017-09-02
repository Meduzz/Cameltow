package example

import java.util.concurrent.TimeUnit

import io.undertow.websockets.core.{BufferedBinaryMessage, BufferedTextMessage, WebSocketChannel}
import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework.datapoints.Ip
import se.chimps.cameltow.framework.feaures.RequestDataToHeaders
import se.chimps.cameltow.framework.handlers._
import se.chimps.cameltow.framework.responsebuilders.{BadRequest, Ok, Redirect, Error => FiveHundred}
import se.chimps.cameltow.framework.{Form, FormItem, Response, Text}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

object Server extends App {

  implicit val timeout = Duration(1L, TimeUnit.SECONDS)

  val routes = Cameltow.routes()

  routes.GET("/", Action.sync { req =>
    val body = <html>
      <head>
        <title>Hello!</title>
      </head>
      <body>
        <h1>Hellö!</h1>
        <div>
          <a href="/static/json.html">Form</a>
        </div>
        <div>
          <a href="/p/fake/file.css">Regexp routing</a>
        </div>
        <div>
          <a href="/error">Error</a>
        </div>
        <div>
          <a href="/static/">Static folder</a>
        </div>
        <div>
          <a href="/cookie">Cookies</a>
        </div>
        <div>
          <a href="/ip">Ip</a>
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
          case FormItem(value) => {
            // I guess this is the correct way to switch charset for a string.
            val bytes = value.getBytes(req.charset)
            Ok.json(s"""{"text":"${new String(bytes, "utf-8")}"}""")
          }
          case _ => BadRequest.text("I dont understand.")
        }
      }
      case any => BadRequest.text("Fix your content-type.")
    }
  }))

  routes.GET("/chat", WebSocket(new WebSocketListenerDelegate {
    override def onError(channel: WebSocketChannel, error: Throwable): Unit = {}

    override def onFullBinaryMessage(channel: WebSocketChannel, message: BufferedBinaryMessage): Unit = {}

    override def onFullTextMessage(channel: WebSocketChannel, message: BufferedTextMessage): Unit = {
      text(message.getData.reverse, channel)
    }

    override def onFullCloseMessage(channel: WebSocketChannel, message: BufferedBinaryMessage): Unit = {}
  }))

  val cookieBody = <html>
    <head>
      <title>Read cookie</title>
    </head>
    <body>
      <div>Cookie contains: %cookie%</div>
      <form method="post" action="/cookie">
        <div>
          <input type="text" name="text" />
        </div>
        <div>
          <button type="submit">Write cookie</button>
          <a href="/clear">Clear cookie</a>
        </div>
      </form>
    </body>
  </html>

  routes.GET("/cookie", Action.sync(req => {
    val value = req.cookie("text").getOrElse("")
    val html = cookieBody.toString.replace("%cookie%", value)

    Ok.html(html)
  }))

  routes.POST("/cookie", Action.sync(req => {
    req.body match {
      case Form(form) => {
        form("text").head match {
          case FormItem(value) => Redirect("/cookie").withCookie("text", value)
          case _ => FiveHundred.html("Form post failed.")
        }
      }
      case _ => FiveHundred.html("Form was not form-encoded.")
    }
  }))

  routes.GET("/clear", Action.sync(req => {
    Redirect("/cookie").withClearCookie("text")
  }))

  routes.GET("/ip", Action.sync(req => {
    val ip = req.header("x-request-ip") match {
      case Some(anIp) => anIp
      case None => ""
    }

    Ok.html(ip)
  }))

  val sub = routes.subroute("/hello")

  sub.GET("/world", Action.sync(req => Ok(Text("Hello world!"))))

  val ipDataPoint = Ip("x-request-ip")

  val server = Cameltow.defaults()
    .activate(RequestDataToHeaders(Seq(ipDataPoint)))
    .handler(routes.handler)
    .listen()

  server.start()

  def errorHandling:PartialFunction[Throwable, Response] = {
    case e:Throwable => FiveHundred.text(e.getMessage)
  }
}