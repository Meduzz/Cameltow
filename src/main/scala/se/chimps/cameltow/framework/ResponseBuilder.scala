package se.chimps.cameltow.framework

import se.chimps.cameltow.framework.parsing.Decoder
import se.chimps.cameltow.templates.Template

/**
 * Created by meduzz on 27/04/14.
 */
trait ResponseBuilder {
  protected def statusCode:Int

  protected var headers:Map[String, String] = Map()

  protected def setHeader(key:String, value:String) = {
    headers = headers + (key -> value)
  }

  def withHeader(key:String, value:String):ResponseBuilder

  def build():Response
}

case class ResponseImpl(statusCode:Int, headers:Map[String, String], body:Option[Array[Byte]]) extends Response

object ResponseBuilders {
  // TODO add appropriate logging to all responses.

  object Ok {
    // REST
    def apply(statusCode:Int):RESTResponseBuilder = new RESTResponseBuilderImpl(statusCode)
    def apply[T](entity:T, statusCode:Int)(implicit decoder:Decoder[T]):RESTResponseBuilder = new RESTResponseBuilderImpl(decoder(entity), statusCode)

    //Html
    def apply():HTMLResponseBuilder = new HTMLResponseBuilderImpl(200)
    def apply(body:String, statusCode:Int)(implicit decoder:Decoder[String]):HTMLResponseBuilder = new HTMLResponseBuilderImpl(decoder(body), statusCode)
    def apply[T](statusCode:Int, body:T)(implicit decoder:Decoder[T]):HTMLResponseBuilder = new HTMLResponseBuilderImpl(decoder(body), statusCode)
    def apply[T](body:T)(implicit decoder:Decoder[T]):HTMLResponseBuilder = new HTMLResponseBuilderImpl(decoder(body), 200)
    def apply(template:Template, statusCode:Int)(implicit decoder:Decoder[Template]):HTMLResponseBuilder = new HTMLResponseBuilderImpl(decoder(template), statusCode)
  }

  object Created {
    def apply(redirectTo:String, statusCode:Int = 201):Response = Redirect(redirectTo, statusCode)
    def apply(urlToSelf:String):Response = new ResponseImpl(201, Map(), Some(s"""{this:"${urlToSelf}"}""".getBytes("utf8")))
  }

  object Deleted {
    def apply(statusCode:Int = 204):Response = new ResponseImpl(statusCode, Map(), None)
  }

  object Error {
    def apply(error:Throwable, statusCode:Int):Response = new ResponseImpl(statusCode, Map("Content-Type" -> "text/plain"), Some(s"${error.getMessage}\n${error.getStackTraceString}".getBytes("utf8")))
    def apply(error:Throwable):Response = new ResponseImpl(500, Map("Content-Type" -> "text/plain"), Some(s"${error.getMessage}\n${error.getStackTraceString}".getBytes("utf8")))
    def apply(message:String, error:Throwable, statusCode:Int) = new ResponseImpl(statusCode, Map("Content-Type" -> "text/plain"), Some(s"${message}\n${error.getStackTraceString}".getBytes("utf8")))
    def apply(message:String, error:Throwable) = new ResponseImpl(500, Map("Content-Type" -> "text/plain"), Some(s"${message}\n${error.getStackTraceString}".getBytes("utf8")))
    def apply(message:String, statusCode:Int) = new ResponseImpl(statusCode, Map("Content-Type" -> "text/plain"), Some(s"${message}".getBytes("utf8")))
    def apply(message:String) = new ResponseImpl(500, Map("Content-Type" -> "text/plain"), Some(s"${message}".getBytes("utf8")))
  }

  object TODO {
    def apply(statusCode:Int = 501):Response = new ResponseImpl(statusCode, Map("Content-Type" -> "text/plain"), Some("TODO - This page has not been implemented yet!".getBytes("utf8")))
  }

  object Redirect {
    def apply(redirectTo:String, statusCode:Int = 301):Response = new ResponseImpl(statusCode, Map("Location" -> redirectTo), None)
  }

  object NotFound {
    def apply() = new ResponseImpl(404, Map("Content-Type" -> "text/html"), Some("<h1>404 - Page not found!</h1>".getBytes("utf8")))
    def apply(message:String, statusCode:Int = 404) = new ResponseImpl(statusCode, Map("Content-Type" -> "text/plain"), Some(s"${message}".getBytes("utf8")))
  }

  trait RESTResponseBuilder extends ResponseBuilder {
    def asJSON:RESTResponseBuilder
    def asXML:RESTResponseBuilder
  }

  trait HTMLResponseBuilder extends ResponseBuilder {
    def as(contentType:String):HTMLResponseBuilder
  }

  private class RESTResponseBuilderImpl(val body:Option[Array[Byte]], override val statusCode:Int) extends RESTResponseBuilder {

    def this(statusCode:Int) = this(None, statusCode)

    override def asJSON:RESTResponseBuilder = {
      headers = headers ++ Map[String, String]("Content-Type" -> "application/json")
      this
    }

    override def asXML:RESTResponseBuilder = {
      headers = headers ++ Map[String, String]("Content-Type" -> "application/xml")
      this
    }

    override def withHeader(key:String, value:String):RESTResponseBuilder = {
      setHeader(key, value)
      this
    }

    override def build():Response = {
      new ResponseImpl(statusCode, headers, body)
    }
  }

  private class HTMLResponseBuilderImpl(val body:Option[Array[Byte]], override val statusCode:Int) extends HTMLResponseBuilder {

    def this(statusCode:Int) = this(None, statusCode)

    override def withHeader(key:String, value:String):HTMLResponseBuilder = {
      setHeader(key, value)
      this
    }

    override def as(contentType:String):HTMLResponseBuilder = {
      headers = headers ++ Map[String, String]("Content-Type" -> contentType)
      this
    }

    override def build():Response = {
      new ResponseImpl(statusCode, headers, body)
    }
  }
}