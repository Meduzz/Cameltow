package example

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework.handlers.Static

object Server extends App {

  val routes = Cameltow.routes()

  routes.GET("/", Static.classpath(listDirectory = true))

  val server = Cameltow.defaults()
    .handler(routes.handler)
    .listen()

  server.start()

}