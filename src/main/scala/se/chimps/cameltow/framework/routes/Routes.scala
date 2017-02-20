package se.chimps.cameltow.framework.routes

import io.undertow.server.{HttpHandler, HttpServerExchange}
import org.slf4j.LoggerFactory
import se.chimps.cameltow.framework.Handler
import se.chimps.cameltow.framework.routes.routing.Tree

import scala.util.matching.Regex

trait Routes {

  private[cameltow] var tree = new Tree

  protected val log = LoggerFactory.getLogger("cameltow")

  def GET(route:String, handler:Handler):Unit = addRoute("GET", route, handler)
  def POST(route:String, handler: Handler):Unit = addRoute("POST", route, handler)
  def PUT(route:String, handler: Handler):Unit = addRoute("PUT", route, handler)
  def DELETE(route:String, handler: Handler):Unit = addRoute("DELETE", route, handler)
  def HEAD(route:String, handler: Handler):Unit = addRoute("HEAD", route, handler)
  def PATCH(route:String, handler: Handler):Unit = addRoute("PATCH", route, handler)

  // TODO to support route groups, I need a way to create Routes without a method.

  private def addRoute(method:String, url:String, handler:Handler):Unit = {
    val (regex, params) = regexify(url)
    val route = Route(url, regex, params, method, handler)

    if (log.isDebugEnabled) {
      log.debug(s"$method $url ($regex) ${handler.getClass.getSimpleName}")
    } else {
      log.info(s"$method $url ${handler.getClass.getSimpleName}")
    }

    val newUrl = if (url.startsWith("/")) {
      url.substring(1)
    } else {
      url
    }

    if (newUrl.isEmpty) {
      tree.add(route)
    } else {
      val tr = newUrl.split("/").foldLeft(tree)((t, a) => t.grow(a))
      tr.add(route)
    }
  }

  private def regexify(url:String):(Regex, Seq[String]) = {
    if (url.contains(":")) {
      var paramNames = Seq[String]()
      val r2 = ":([a-zA-Z0-9]*|[a-zA-Z0-9]*(.*))".r

      val newItems = url.substring(1).split("/").map(u => {
        u match {
          case r2(paramName, regex) => {
            val index = paramName.indexOf("(")
            val name  = if (index != -1) {
              paramName.substring(0, index)
            } else {
              paramName
            }

            paramNames = paramNames ++ Seq(name)
            if (regex != null) {
              regex
            } else {
              "([a-zA-Z0-9]+)"
            }
          }
          case r2(name) => {
            "([a-zA-Z0-9]+)"
          }
          case _ => u
        }
      })

      (s"/${newItems.mkString("/")}".r, paramNames)
    } else {
      (url.r, Seq())
    }
  }

  def handler:Handler
}

case class Route(raw:String, route:Regex, names:Seq[String], method:String, action:Handler)

class RoutingImpl extends Routes {
  override def handler: Handler = new Handler {
    override def httpHandler: HttpHandler = new HttpHandler {
      override def handleRequest(exchange: HttpServerExchange): Unit = {
        val url = if (exchange.getRelativePath.isEmpty) {
          exchange.getRequestPath
        } else {
          exchange.getRelativePath
        }

        val newUrl = if (url.startsWith("/")) {
          url.substring(1)
        } else {
          url
        }

        val candidates: Seq[Route] = if (newUrl.isEmpty) {
          tree.values()
        } else {
          newUrl
            .split("/")
            .foldLeft(Seq(tree))((t, a) => t.flatMap(_.matches(a)))
            .flatMap(_.values())
        }

        val action = candidates
          .filter(_.method == exchange.getRequestMethod.toString) // TODO add || _.method == "*" to handle route groups?
          .find(action => { // TODO using find opens up a can of Action bingo. Not really a framework problem anyhow.
            val m = action.route.pattern.matcher(url)

            if (m.matches()) {
              val groupVals = (1 to m.groupCount()).map(i => m.group(i))
              action.names.zip(groupVals).foreach(kv => {
                val (key, value) = kv
                exchange.addPathParam(key, value)
              })

              val newPath = if (action.names.nonEmpty && action.route.regex.split("/").reverse.head.contains(".*)")) {
                var lastIndex = url.lastIndexOf("/")
                if (lastIndex == -1) {
                  lastIndex = 0
                }
                exchange.getRelativePath.drop(lastIndex)
              } else {
                exchange.getRelativePath.drop(url.length)
              }

              if (newPath.isEmpty) {
                exchange.setRelativePath("/")
              } else {
                exchange.setRelativePath(newPath)
              }

              true
            } else {
              false
            }
          })

        action.map(_.action)
          .getOrElse(Handler.NOT_FOUND)
          .httpHandler.handleRequest(exchange)
      }
    }
  }
}