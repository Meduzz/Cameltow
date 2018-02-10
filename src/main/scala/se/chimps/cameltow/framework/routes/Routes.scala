package se.chimps.cameltow.framework.routes

import io.undertow.server.{HttpHandler, HttpServerExchange}
import org.slf4j.LoggerFactory
import se.chimps.cameltow.framework.Handler
import se.chimps.cameltow.framework.routes.routing.Tree

import scala.util.matching.Regex

trait Routes {

  private[cameltow] var tree = new Tree

  protected val log = LoggerFactory.getLogger("cameltow")

  def base:String

  def GET(route:String, handler:Handler):Unit = addRoute("GET", route, handler)
  def POST(route:String, handler: Handler):Unit = addRoute("POST", route, handler)
  def PUT(route:String, handler: Handler):Unit = addRoute("PUT", route, handler)
  def DELETE(route:String, handler: Handler):Unit = addRoute("DELETE", route, handler)
  def HEAD(route:String, handler: Handler):Unit = addRoute("HEAD", route, handler)
  def PATCH(route:String, handler: Handler):Unit = addRoute("PATCH", route, handler)

  def subroute(path:String):Routes = {
    val r = new RoutingImpl(path)
    addRoute("*", path, r.handler)
    r
  }

  def subroute(path:String, func:(Routes)=>Handler):Routes = {
    val r = new RoutingImpl(path)
    addRoute("*", path, func(r))
    r
  }

  protected def addRoute(method:String, url:String, handler:Handler):Unit = {
    val (regex, params) = regexify(url)
    val route = Route(url, regex, params, method, handler)

    if (log.isDebugEnabled) {
      log.debug(s"$method ${append(url)} ($regex) ${handler.getClass.getSimpleName}")
    } else {
      log.info(s"$method ${append(url)} ${handler.getClass.getSimpleName}")
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

  def dropRoute(url:String):Unit = {
    val newUrl = if (url.startsWith("/")) {
      url.substring(1)
    } else {
      url
    }

    if (newUrl.nonEmpty) {
      tree.remove(newUrl)
    }
  }

  private def regexify(url:String):(Regex, Seq[String]) = {
    if (url.contains(":")) {
      var paramNames = Seq[String]()
      val r2 = ":([a-zA-Z0-9]*|[a-zA-Z0-9]*(.*))".r

      val newItems = url.substring(1).split("/").map {
        case r2(paramName, regex) => {
          val index = paramName.indexOf("(")
          val name = if (index != -1) {
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
        case u => u
      }

      (s"/${newItems.mkString("/")}".r, paramNames)
    } else {
      (url.r, Seq())
    }
  }

  protected def append(path:String):String = {
    if (base.length > 1) {
      base ++ path
    } else {
      path
    }
  }

  def handler:Handler
}

case class Route(raw:String, route:Regex, names:Seq[String], method:String, action:Handler)

class RoutingImpl(val base:String = "/") extends Routes {
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
            .foldLeft(Seq(tree))((t, a) => t.flatMap(branch => {
              val branches = branch.matches(a)

              if (branches.isEmpty) {
                Seq(branch)
              } else {
                branches
              }
            }))
            .flatMap(_.values())
        }

        val action = candidates
          .filter(c => c.method == exchange.getRequestMethod.toString || c.method == "*")
          .find(action => { // TODO using find opens up a can of Action bingo. Not really a framework problem anyhow.
            val m = action.route.pattern.matcher(url)

            if (m.matches()) {
              val groupVals = (1 to m.groupCount()).map(i => m.group(i))
              action.names.zip(groupVals).foreach(kv => {
                val (key, value) = kv
                exchange.addPathParam(key, value)
              })

              val newPath = if (action.names.nonEmpty && action.route.regex.split("/").reverse.head.contains(".*)")) {
                val count = action.raw.split("/").length
                val partialUrl = url.split("/").take(count).mkString("/")
                var lastIndex = partialUrl.lastIndexOf("/")
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
            } else if (action.method == "*") {
              val count = action.raw.split("/").length
              val partialUrl = url.split("/").take(count).mkString("/")

              val m = action.route.pattern.matcher(partialUrl)

              if (m.matches()) {
                val groupVals = (1 to m.groupCount()).map(i => m.group(i))
                action.names.zip(groupVals).foreach(kv => {
                  val (key, value) = kv
                  exchange.addPathParam(key, value)
                })

                val newPath = if (action.names.nonEmpty && action.route.regex.split("/").reverse.head.contains(".*)")) {
                  var lastIndex = partialUrl.lastIndexOf("/")
                  if (lastIndex == -1) {
                    lastIndex = 0
                  }
                  exchange.getRelativePath.drop(lastIndex)
                } else {
                  exchange.getRelativePath.drop(partialUrl.length)
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