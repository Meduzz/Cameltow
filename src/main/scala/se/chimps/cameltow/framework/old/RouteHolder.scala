package se.chimps.cameltow.framework.old

import se.chimps.cameltow.framework.old.Http._

import scala.collection.mutable

/**
 * Created by meduzz on 14/05/14.
 */
object RouteHolder {
  private val mapping = mutable.Map[String, mutable.Map[Method, BasicAction]]()

  def put(verb:Method, path:String, action:BasicAction) = {
    mapping.get(path) match {
      case None => mapping += (path->mutable.Map(verb->action))
      case Some(map:mutable.Map[Method, BasicAction]) => map += (verb->action)
    }
  }

  def iterator:Iterator[String] = {
    mapping.keysIterator
  }

  def get(route:String):Map[Method, BasicAction] = {
    mapping(route).toMap
  }
}
