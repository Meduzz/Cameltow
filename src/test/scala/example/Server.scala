package example

import se.chimps.cameltow.Cameltow
import se.chimps.cameltow.framework._
import se.chimps.cameltow.framework.parsing.Decoder
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import se.chimps.cameltow.framework.ResponseBuilders.Ok
import se.chimps.cameltow.modules.Sprits

/**
 * Created by meduzz on 24/04/14.
 */
object Server extends Cameltow with Sprits {
  def initialize() = {
    registerController(Controller)
    addStaticContent("/static", new File(getClass.getClassLoader.getResource("form.html").getFile).getParentFile)
    listen(8080)
  }
}

object Controller extends SpritsController {
  import ResponseBuilders.Error

  val counterService = new AtomicVisitService()
  implicit val decoder = StringDecoder

  override def apply() = {
    get("/asdf", BasicAction { req =>
      logger.info("{}", req)
      counterService.visit()
      Ok("Hello world!").build()
    })

    get("/isMeduzz", Authorized { auth =>
      counterService.visit()
      if (auth) {
        logger.info("{}", auth)
        Ok("Hello and welcome back Meduzz!").build()
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
      Ok(s"You sent: ${req.params("apa")}").build()
    })

    get("/plain", BasicAction {
      counterService.visit()
      Ok(200).build()
    })

    post("/post", BasicAction { req =>
      logger.info("{}", req)
      counterService.visit()
      Ok(s"You posted: ${req.params("text")}.").build()
    })

    get("/visits", BasicAction {
      counterService.visit()
      Ok(s"# of visits (${counterService.visits()})").build()
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