package se.chimps.cameltow.framework

import se.chimps.cameltow.logging.Logging
import se.chimps.cameltow.framework.Http.{Methods, Method}

/**
 * Created by meduzz on 24/04/14.
 */
trait SpritsController extends Routing {
  // TODO add a config
  def apply()
}

trait Routing extends Logging {
  protected def get(path:String, action:BasicAction) = {
    logger.info("GET handler added to path: {}.", path)
    RouteHolder.put(Methods.GET, path, action)
  }

  protected def post(path:String, action:BasicAction) = {
    logger.info("POST handler added to path: {}.", path)
    RouteHolder.put(Methods.POST, path, action)
  }

  protected def put(path:String, action:BasicAction) = {
    logger.info("PUT handler added to path: {}.", path)
    RouteHolder.put(Methods.PUT, path, action)
  }

  protected def delete(path:String, action:BasicAction) = {
    logger.info("DELETE handler added to path: {}.", path)
    RouteHolder.put(Methods.DELETE, path, action)
  }

  protected def head(path:String, action:BasicAction) = {
    logger.info("HEAD handler added to path: {}.", path)
    RouteHolder.put(Methods.HEAD, path, action)
  }

  protected def options(path:String, action:BasicAction) = {
    logger.info("OPTIONS handler added to path: {}.", path)
    RouteHolder.put(Methods.OPTIONS, path, action)
  }

  protected def trace(path:String, action:BasicAction) = {
    logger.info("TRACE handler added to path: {}.", path)
    RouteHolder.put(Methods.TRACE, path, action)
  }
}

trait BasicAction extends (Request => Response) {
  def apply() = this
}

object BasicAction {
  def apply(f:Request => Response):BasicAction = new BasicAction {
    override def apply(req: Request): Response = f(req)
  }

  def apply(f: => Response):BasicAction = new BasicAction {
    override def apply(req: Request): Response = f
  }
}

// TODO add session and cookie
// TODO change Map[String, Option[String]] to Map[String, Either[Option[String], Option[Array[String]]]
case class Request(method:Method, headers:Map[String, String], params:Map[String, String], body:Option[Array[Byte]])

trait Response {
  def statusCode:Int
  def headers:Map[String, String]
  def body:Option[Array[Byte]]
}
