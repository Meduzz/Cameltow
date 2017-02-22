package se.chimps.cameltow.framework

import java.util.Date

import io.undertow.server.handlers.{CookieImpl, Cookie => Kaka}

class Cookie(val name:String, val value:String, expires:Option[Date] = None) {
  val undertowCookie = new CookieImpl(name, value)

  expires.foreach(d => undertowCookie.setExpires(d))

  def setPath(path:String):Unit = undertowCookie.setPath(path)
}
