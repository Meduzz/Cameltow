package se.chimps.cameltow.framework.routes

import scala.concurrent.ExecutionContext

trait Routes {

  def GET(path:String, handler:Handler)(implicit ec:ExecutionContext)
  def POST(path:String, handler:Handler)(implicit ec:ExecutionContext)
  def PUT(path:String, handler:Handler)(implicit ec:ExecutionContext)
  def DELETE(path:String, handler:Handler)(implicit ec:ExecutionContext)
  def HEAD(path:String, handler:Handler)(implicit ec:ExecutionContext)
  def TRACE(path:String, handler:Handler)(implicit ec:ExecutionContext)
  def PATCH(path:String, handler:Handler)(implicit ec:ExecutionContext)

  def route(path:String):Routes

  /*
  io/undertow/server/handlers/resource/ResourceHandler.java
  io/undertow/server/handlers/resource/PathResourceManager.java
   */
  //def static(dir:String)
  /*
  io/undertow/server/handlers/resource/ResourceHandler.java
  io/undertow/server/handlers/resource/FileResourceManager.java
   */
  //def staticFile(file:String)

}
