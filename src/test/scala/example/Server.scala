package example

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework._
import se.chimps.cameltow.framework.parsing.Decoder
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by meduzz on 24/04/14.
 */
object Server extends Cameltow with Sprits {
  def initialize() = {
    registerController(Controller)
    staticContent("/static", new File("src/test/resources/form.html").getParentFile)
    listen(8080)
  }
}

object Controller extends SpritsController {
  import ResponseBuilders.RestResponseBuilder._
  import ResponseBuilders.Error

  val counterService = new AtomicVisitService()

  override def apply() = {
    get("/asdf", BasicAction { req =>
      logger.info("{}", req)
      counterService.visit()
      Ok().withEntity("Hello world!")(StringDecoder).build()
    })

    get("/isMeduzz", Authorized { auth =>
      counterService.visit()
      if (auth) {
        logger.info("{}", auth)
        Ok().withEntity("Hello and welcome back Meduzz!")(StringDecoder).build()
      } else {
        Error(new Exception("You are not welcome here! Name must be Meduzz."))
      }
    }(req => req.params.get("name") match {
      case Some("Meduzz") => true
      case _ => false
    }))

    get("/q/{apa}", BasicAction { req =>
      logger.info("{}", req)
      counterService.visit()
      Ok().withEntity(s"You sent: ${req.params("apa")}")(StringDecoder).build()
    })

    get("/plain", BasicAction {
      counterService.visit()
      Ok(200).build()
    })

    post("/post", BasicAction { req =>
      logger.info("{}", req)
      counterService.visit()
      Ok().withEntity(s"You posted: ${req.params("text")}.")(StringDecoder).build()
    })

    get("/visits", BasicAction {
      counterService.visit()
      Ok().withEntity(s"# of visits (${counterService.visits()})")(StringDecoder).build()
    })
  }
}

object StringDecoder extends Decoder[String] {
  def apply(e:String):Option[Array[Byte]] = {
    Some(e.getBytes())
  }
}

trait Authorized extends BasicAction {
  def apply(authed:Boolean):Response
}

object Authorized {
  def apply(ctrl:Boolean=>Response)(f:Request=>Boolean):Authorized = new Authorized {
    override def apply(authed:Boolean):Response = ctrl(authed)

    override def apply(req:Request):Response = this(f(req))
  }
}

class AtomicVisitService {
  private val counter:AtomicInteger = new AtomicInteger(0)

  def visit() = {
    counter.incrementAndGet()
  }

  def visits():Int = {
    counter.get()
  }
}