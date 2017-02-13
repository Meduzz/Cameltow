package se.chimps.cameltow.framework

import org.scalatest.FunSuite
import se.chimps.cameltow.framework.old.Http.Methods
import se.chimps.cameltow.framework.common.TestAction
import se.chimps.cameltow.framework.old.{BasicAction, RouteHolder}
import se.chimps.cameltow.framework.old.ResponseBuilders.{Ok, TODO}


/**
 * Created by meduzz on 24/05/14.
 */
class RouteHolderTest extends FunSuite {
  test("happy case") {
    RouteHolder.put(Methods.GET, "/test", BasicAction { req => Ok().build() })

    val it = RouteHolder.iterator
    assert(it.hasNext)

    val next = it.next()
    assert(RouteHolder.get(next) != null)
  }

  test("adding same path&method again replaces") {
    val action1 = BasicAction { req => Ok().build() }
    RouteHolder.put(Methods.GET, "/test2", action1)

    val action2 = BasicAction { req => TODO() }
    RouteHolder.put(Methods.GET, "/test2", action2)

    val it:Iterator[String] = RouteHolder.iterator
    assert(it.hasNext)

    it.next // happy case test lives here :)
    val next = it.next
    val action3 = RouteHolder.get(next).get(Methods.GET).get

    assert(!action3.equals(action1))
    assert(action3.equals(action2))
  }
}
