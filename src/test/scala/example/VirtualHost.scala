package example

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework.handlers.Action
import se.chimps.cameltow.framework.responsebuilders.Ok

import scala.concurrent.ExecutionContext.Implicits.global

object VirtualHost extends App {
  val vhosts = Cameltow.virtualHosts()

  val host1 = Cameltow.routes()
  val host2 = Cameltow.routes()

  host1.GET("/", Action.sync(req => {
    Ok.text("Hello, host1 here.")
  }))

  host2.GET("/", Action.sync(req => {
    Ok.text("Hello, host2 here.")
  }))

  vhosts.addHandler("host1", host1.handler)
  vhosts.addHandler("host2", host2.handler)

  val server = Cameltow.defaults()
    .handler(vhosts.handler)
    .listen()

  server.start()
}
