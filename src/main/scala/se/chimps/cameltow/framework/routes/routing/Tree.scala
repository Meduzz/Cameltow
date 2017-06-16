package se.chimps.cameltow.framework.routes.routing

import se.chimps.cameltow.framework.routes.Route

class Tree {
  private var branches:Map[String, Tree] = Map()
  private var data:Seq[Route] = Seq()

  def grow(branch:String):Tree = {
    val translated = translate(branch)

    if (branches.contains(translated)) {
      branches(translated)
    } else {
      val tree = new Tree
      branches = branches ++ Map(translated -> tree)
      tree
    }
  }

  def matches(branch:String):Seq[Tree] = {
    branches.filter(kv => {
      val (key, _) = kv
      branch.matches(key)
    }).values.toSeq
  }

  def add(route: Route):Unit = data = data ++ Seq(route)

  def remove(url:String):Unit = {
    val split = url.split("/")

    if (split.length > 1) {
      matches(translate(split.head)).foreach(_.remove(split.tail.mkString("/")))
    } else {
      data = data.filterNot(_.raw == translate(url))
      branches = branches.filterKeys(_ != translate(url))
    }
  }

  def values():Seq[Route] = data

  private def translate(in:String):String = {
    if (in.contains(":")) {
      val r = ":([a-zA-Z0-9]*)".r
      r.replaceAllIn(in, "([a-zA-Z0-9]+)")
    } else {
      in
    }
  }
}
