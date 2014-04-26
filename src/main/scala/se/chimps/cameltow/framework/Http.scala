package se.chimps.cameltow.framework

/**
 * Created by meduzz on 25/04/14.
 */
object Http {
  case class Method(verb:String)

  object Methods {
    val GET = Method("GET")
    val POST = Method("POST")
    val PUT = Method("PUT")
    val DELETE = Method("DELETE")
    val HEAD = Method("HEAD")
    val OPTIONS = Method("OPTIONS")
    val TRACE = Method("TRACE")
  }
}
