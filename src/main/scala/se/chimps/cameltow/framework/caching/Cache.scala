package se.chimps.cameltow.framework.caching

import se.chimps.cameltow.framework.Request

/**
 * Created by meduzz on 11/05/14.
 */
trait Cache[T] {
  def put(key:String, item:T)
  def get(key:String):T
  def clear(key:String):T

  def cacheThrough(key:String, onCacheMiss:(Request)=>T):T
}

trait Cachable {
  // TODO add serialization etc here...
}
