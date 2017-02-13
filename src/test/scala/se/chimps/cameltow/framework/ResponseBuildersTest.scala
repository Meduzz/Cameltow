package se.chimps.cameltow.framework

import org.scalatest.FunSuite
import se.chimps.cameltow.framework.old.ResponseBuilders._
import example.StringDecoder

import scala.Some

/**
 * Created by meduzz on 29/05/14.
 */
class ResponseBuildersTest extends FunSuite {
  test("Ok builder, happy cases") {
    implicit val decoder = StringDecoder

    val rest1 = Ok(200).asJSON.build()
    val rest2 = Ok[String]("Spam", 200).asXML.build()

    assert(rest1.statusCode == 200)
    assert(rest1.body.equals(None))
    assert(rest1.headers("Content-Type").equals("application/json"))

    assert(rest2.statusCode == 200)
    assert(rest2.body match {
      case Some(body:Array[Byte]) => new String(body, "utf-8").equals("Spam")
      case None => false
    })
    assert(rest2.headers("Content-Type").equals("application/xml"))

    val html1 = Ok().build()
    val html2 = Ok("Spam").build()

    assert(html1.statusCode == 200)
    assert(html1.body.equals(None))
    assert(html1.headers.size == 2)
    assert(html1.headers("Content-Type").equals("text/html"))
    assert(html2.statusCode == 200)
    assert(html2.body match {
      case Some(body:Array[Byte]) => new String(body, "utf-8").equals("Spam")
      case None => false
    })
    assert(html2.headers("Content-Length").equals("4"))
    assert(html2.headers("Content-Type").equals("text/html"))
  }

  test("Created builder, happy cases") {
    val redirectTo = Created("/redirectTo", 201)
    val created = Created("/created")

    assert(redirectTo.statusCode == 201)
    assert(redirectTo.headers("Location").equals("/redirectTo"))

    assert(created.statusCode == 201)
    assert(created.headers.size == 0)
    assert(!created.body.equals(None))
  }

  test("Deleted builder, the happy case") {
    val deleted = Deleted()

    assert(deleted.isInstanceOf[old.Response])
    assert(deleted.statusCode == 204)
    assert(deleted.body.equals(None))
  }

  test("Error builder, happy case") {
    val error1 = Error(new NullPointerException("null"))
    val error2 = Error("null", new NullPointerException("null"))
    val error3 = Error("null")

    assert(error1.isInstanceOf[old.Response])
    assert(error1.statusCode == 500)
    assert(!error1.body.equals(None))

    assert(error2.isInstanceOf[old.Response])
    assert(error2.statusCode == 500)
    assert(!error2.body.equals(None))

    assert(error3.isInstanceOf[old.Response])
    assert(error3.statusCode == 500)
    assert(!error3.body.equals(None))
  }

  test("Redirect builder, happy case") {
    val redirect = Redirect("/home")

    assert(redirect.isInstanceOf[old.Response])
    assert(redirect.statusCode == 301)
    assert(redirect.body.equals(None))
  }

  test("NotFound builder, happy case") {
    val nf1 = NotFound()
    val nf2 = NotFound("404 tbh")

    assert(nf1.isInstanceOf[old.Response])
    assert(nf1.statusCode == 404)
    assert(!nf1.body.equals(None))

    assert(nf2.isInstanceOf[old.Response])
    assert(nf2.statusCode == 404)
    assert(!nf2.body.equals(None))
  }
}
