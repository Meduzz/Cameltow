package se.chimps.cameltow.framework

import java.nio.file.Path

import scala.collection.mutable.Queue
import scala.concurrent.Future

trait Body
case class Form(data:Map[String, Seq[FormValue]]) extends Body
case class Encoded(bytes:Future[Array[Byte]]) extends Body
case class Stream(queue:Queue[Array[Byte]]) extends Body

trait FormValue
case class FormItem(value:String) extends FormValue
case class FileItem(file:Path) extends FormValue