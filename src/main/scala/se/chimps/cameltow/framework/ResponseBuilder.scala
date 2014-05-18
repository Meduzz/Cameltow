package se.chimps.cameltow.framework

import se.chimps.cameltow.framework.parsing.Decoder

/**
 * Created by meduzz on 27/04/14.
 * TODO rework so starters (Ok etc) works a bit smarter and triggers different responsbuilders depending on what they are sent. Like Entities for REST style and Template for Html style.
 */
trait ResponseBuilder {
  def statusCode:Int
  var headers:Map[String, String] = Map()

  protected def setHeader(key:String, value:String) = {
    headers = headers + (key -> value)
  }

  def withHeader(key:String, value:String):ResponseBuilder
  def build():Response
}

case class ResponseImpl(statusCode:Int, headers:Map[String, String], body:Option[Array[Byte]]) extends Response

object ResponseBuilders {
  // TODO add appropriate logging to all responses.
  object RestResponseBuilder {

    trait RESTResponseBuilder extends ResponseBuilder {
      def withEntity[T](entity:T)(implicit decoder:Decoder[T]):RESTResponseBuilder
      def asJSON:RESTResponseBuilder
      def asXML:RESTResponseBuilder
    }

    private class RESTResponseBuilderImpl(override val statusCode:Int) extends RESTResponseBuilder{
      var body:Option[Array[Byte]] = None

      override def asJSON:RESTResponseBuilder = {
        headers = headers ++ Map[String, String]("Content-Type" -> "application/json")
        this
      }

      override def withEntity[T](entity: T)(implicit decoder: Decoder[T]): RESTResponseBuilder = {
        body = decoder(entity)
        this
      }

      override def asXML: RESTResponseBuilder = {
        headers = headers ++ Map[String, String]("Content-Type" -> "application/xml")
        this
      }

      override def withHeader(key: String, value: String):RESTResponseBuilder = {
        setHeader(key, value)
        this
      }

      override def build(): Response = {
        new ResponseImpl(statusCode, headers, body)
      }
    }

    object Ok {
      def apply(statusCode: Int = 200): RESTResponseBuilder = new RESTResponseBuilderImpl(statusCode)
    }

    object Created {
      def apply(location: String, statusCode: Int = 201): Response = Redirect(location, statusCode)
    }

    object Deleted {
      def apply(statusCode:Int=204):Response = new ResponseImpl(statusCode, Map(), None)
    }
  }

  object HtmlResponseBuilder {

    trait HTMLResponseBuilder extends ResponseBuilder {
      def withBody[T](body:T)(implicit decoder:Decoder[T]):HTMLResponseBuilder
      def as(contentType:String):HTMLResponseBuilder
    }

    private class HTMLResponseBuilderImpl(override val statusCode:Int) extends HTMLResponseBuilder {
      var body:Option[Array[Byte]] = None

      override def withBody[T](entity: T)(implicit decoder: Decoder[T]): HTMLResponseBuilder = {
        body = decoder(entity)
        this
      }

      override def withHeader(key: String, value: String): HTMLResponseBuilder = {
        setHeader(key, value)
        this
      }

      override def as(contentType: String): HTMLResponseBuilder = {
        headers = headers ++ Map[String, String]("Content-Type" -> contentType)
        this
      }

      override def build(): Response = {
        new ResponseImpl(statusCode, headers, body)
      }
    }

    object Ok {
      def apply(statusCode:Int=200):HTMLResponseBuilder = new HTMLResponseBuilderImpl(statusCode)
    }
  }

  object Error {
    def apply(error:Throwable, statusCode:Int=500):Response = new ResponseImpl(statusCode, Map("Content-Type" -> "text/plain"), Some(s"${error.getMessage}\n${error.getStackTraceString}".getBytes("utf8")))
  }

  object TODO {
    def apply(statusCode:Int=501):Response = new ResponseImpl(statusCode, Map("Content-Type" -> "text/plain"), Some("TODO - This page has not been implemented yet!".getBytes("utf8")))
  }

  object Redirect {
    def apply(url: String, statusCode:Int=301): Response = new ResponseImpl(statusCode, Map("Location" -> url), None)
  }

  object NotFound {
    def apply(statusCode: Int = 404) = new ResponseImpl(statusCode, Map("Content-Type" -> "text/html"), Some("<h1>404 - Page not found!</h1>".getBytes("utf8")))
  }
}