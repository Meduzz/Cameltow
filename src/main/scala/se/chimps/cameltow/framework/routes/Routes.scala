package se.chimps.cameltow.framework.routes

import se.chimps.cameltow.framework.Handler

import scala.concurrent.ExecutionContext

trait Routes {

  def GET(path:String, handler:Handler)(implicit ec:ExecutionContext):Routes
  def POST(path:String, handler:Handler)(implicit ec:ExecutionContext):Routes
  def PUT(path:String, handler:Handler)(implicit ec:ExecutionContext):Routes
  def DELETE(path:String, handler:Handler)(implicit ec:ExecutionContext):Routes
  def HEAD(path:String, handler:Handler)(implicit ec:ExecutionContext):Routes
  def TRACE(path:String, handler:Handler)(implicit ec:ExecutionContext):Routes
  def PATCH(path:String, handler:Handler)(implicit ec:ExecutionContext):Routes

  def route(path:String):Routes

  def handler:Handler
}
