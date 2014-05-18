package se.chimps.cameltow.framework.undertow

import io.undertow.util.{HeaderValues, HttpString, HeaderMap}
import java.util

/**
 * Created by meduzz on 27/04/14.
 */
object HeaderMapWrapper {
  def apply(headers:HeaderMap):Map[String, String] = {
    new HeaderMapWrapper(headers)
  }
}

private class HeaderMapWrapper(headers:HeaderMap) extends Map[String, String] {
  override def +[B1 >: String](kv: (String, B1)): Map[String, B1] = {
    headers.put(new HttpString(kv._1), kv._2.asInstanceOf[String])
    this
  }

  override def get(key: String): Option[String] = {
    if (headers.contains(key)) {
      Some(headers.get(key, 0))
    } else {
      None
    }
  }

  override def apply(key:String):String = {
    get(key).getOrElse(null)
  }

  override def iterator: Iterator[(String, String)] = {
    new HeaderMapIterator(headers.iterator())
  }

  override def -(key: String): Map[String, String] = {
    if (headers.contains(key)) {
      headers.remove(key)
    }
    this
  }
}

private class HeaderMapIterator(val it:util.Iterator[HeaderValues]) extends Iterator[(String, String)] {
  override def hasNext: Boolean = it.hasNext

  override def next(): (String, String) = {
    val next = it.next()
    val key = next.getHeaderName.toString
    val vals = next.getFirst // TODO rework!
    (key, vals)
  }
}