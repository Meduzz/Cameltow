package se.chimps.cameltow.framework.routes.routing

import org.scalatest.{FunSuite, Matchers}

class TreeTest extends FunSuite with Matchers {

  test("a tree grows and can be navigated") {
    val tree = new Tree

    val wildcardTree = tree.grow("a").grow(":b")
    wildcardTree.grow("c")
    wildcardTree.grow("a")

    val failedTree = tree.matches("d")
    failedTree shouldBe empty

    val a = tree.matches("a")
    a shouldNot be(empty)
    a.size shouldBe 1

    val b = a.head.matches("b")
    b shouldNot be(empty)
    b.size shouldBe 1

    val failedBranch = b.head.matches("d")
    failedBranch shouldBe empty

    val c = b.head.matches("c")
    c shouldNot be(empty)
    c.size shouldBe 1

    val aba = b.head.matches("a")
    aba shouldNot be(empty)
    aba.size shouldBe 1
  }

  test("grow are idempotentish") {
    val tree = new Tree

    val a = tree.grow("a")
    val b = tree.grow(":b")
    val q = tree.grow("a")
    val q2 = tree.grow(":c")

    q shouldBe a
    q2 shouldBe b
  }

  test("removing stuff can be done") {
    val tree = new Tree

    tree.grow("a")
    val b = tree.grow("b")
    tree.grow("c").grow("d")

    tree.matches("c").head.matches("d").headOption shouldNot be(empty)
    tree.remove("c/d")
    tree.matches("c").head.matches("d").headOption shouldBe empty

    tree.remove("a")
    tree.matches("a").headOption shouldBe empty

    tree.matches("b").head shouldBe b
  }
}
