package se.chimps.cameltow.framework.undertow

/**
 * Created by meduzz on 27/04/14.
 */
object RequestParameterWrapper {
  def apply(params:java.util.Map[String, java.util.Deque[String]]):Map[String, String] = {
    new RequestParameterWrapper(params)
  }
}

// TODO throws java.util.NoSuchElementException: key not found: <key>. When using mapInstance(key) call.
private class RequestParameterWrapper(val params:java.util.Map[String, java.util.Deque[String]]) extends Map[String, String] {
  override def +[B1 >: String](kv: (String, B1)): Map[String, B1] = {
    if (!params.containsKey(kv._1)) {
      params.put(kv._1, new java.util.ArrayDeque[String](2))
    }

    val que = params.get(kv._1)
    que.add(kv._2.toString)
    this
  }

  override def get(key: String): Option[String] = {
    if (params.containsKey(key)) {
      Some(params.get(key).getFirst) // TODO rework!
    } else {
      None
    }
  }

  override def apply(key:String):String = {
    get(key).getOrElse(null)
  }

  override def iterator: Iterator[(String, String)] = {
    new RequestParamsIterator(params.keySet().iterator(), params)
  }

  override def -(key: String): Map[String, String] = {
    params.remove(key)
    this
  }
}

private class RequestParamsIterator(val it:java.util.Iterator[String], val params:java.util.Map[String, java.util.Deque[String]]) extends Iterator[(String, String)] {
  override def hasNext: Boolean = it.hasNext

  override def next(): (String, String) = {
    val next = it.next()
    val vals = params.get(next).getFirst // TODO rework.
    (next, vals)
  }
}
