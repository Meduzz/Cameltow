package se.chimps.cameltow.framework.handlers

import java.nio.ByteBuffer
import java.nio.file.{Files, LinkOption, Path}

import io.undertow.server.handlers.cache.DirectBufferCache
import io.undertow.server.handlers.resource.{CachingResourceManager, ClassPathResourceManager, PathResourceManager, ResourceManager, ResourceHandler => UndertowResourceHandler}
import io.undertow.server.{HttpHandler, HttpServerExchange}
import io.undertow.util.HttpString
import org.xnio.BufferAllocator
import se.chimps.cameltow.framework.Handler

/**
  * Static Handler-companion with reasonable defaults.
  */
object Static {
  def file(file:Path, contentType:String):Handler = new StaticFileHandler(file, contentType)
  def path(path:Path, welcomeFiles:Seq[String] = Seq("index.html"), listDirectory:Boolean = false, caching:Option[Caching] = None):Handler = new StaticPathHandler(path, welcomeFiles, listDirectory, caching)
  def classpath(prefix:String = "", welcomeFiles:Seq[String] = Seq("index.html"), listDirectory:Boolean = false, caching:Option[Caching] = None):Handler = new ClasspathHandler(prefix, welcomeFiles, listDirectory, caching)
}

class StaticFileHandler(val file:Path, contentType:String) extends Handler {
  override def httpHandler:HttpHandler = new HttpHandler {
    override def handleRequest(exchange: HttpServerExchange): Unit = {
      if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
        val content = Files.readAllBytes(file)
        exchange.setStatusCode(200)
        exchange.getResponseHeaders.put(HttpString.tryFromString("Content-Type"), contentType)
        exchange.getResponseSender.send(ByteBuffer.wrap(content))
      } else {
        exchange.setStatusCode(404)
      }
    }
  }
}

class StaticPathHandler(val path:Path, welcomeFiles:Seq[String], listDirectory:Boolean, caching:Option[Caching]) extends Handler {
  override def httpHandler:UndertowResourceHandler = {
    val manager = new PathResourceManager(path, 1024L)

    val handler = ResourceHandler(caching.map(_.cache(manager)).getOrElse(manager))

    if (welcomeFiles.nonEmpty) {
      handler.setWelcomeFiles(welcomeFiles: _*)
    }

    handler.setDirectoryListingEnabled(listDirectory)

    handler
  }
}

class ClasspathHandler(val prefix:String, welcomeFiles:Seq[String], listDirectory:Boolean, caching:Option[Caching]) extends Handler {
  override def httpHandler:HttpHandler = {
    val manager = new ClassPathResourceManager(classOf[ClasspathHandler].getClassLoader, prefix)

    val handler = ResourceHandler(caching.map(_.cache(manager)).getOrElse(manager))

    if (welcomeFiles.nonEmpty) {
      handler.setWelcomeFiles(welcomeFiles: _*)
    }

    handler.setDirectoryListingEnabled(listDirectory)

    handler
  }
}

/**
  * This will create 2 caches.
  * First a LRU cache for metadata (what exists and not) with maxItemCount slots and max age set to maxAgeMillis.
  * Second, there will be a cache of (memory mapped?) buffers, taking up at most maxSizeMb with max age set to maxAgeMillis.
  * ... I think. ;)
  *
  * @param maxItemCount - the max number of metadata items to cache.
  * @param maxAgeMillis - the max age, in millis a cached item can have. Turn off with -1.
  * @param maxSizeMb - the max size in MB of data to cache.
  */
case class Caching(maxItemCount:Int, maxAgeMillis:Int, maxSizeMb:Int) {
  private[cameltow] def cache(child:ResourceManager):ResourceManager = {
    // WTF is cache slizes and pages, and maxSize in what? Blue pigs in the bushes?
    new CachingResourceManager(maxItemCount, 1024 * 1024, new DirectBufferCache(1024, 100, maxSizeMb * 1024, BufferAllocator.DIRECT_BYTE_BUFFER_ALLOCATOR, maxAgeMillis), child, maxAgeMillis)
  }
}

private object ResourceHandler {
  def apply(manager:ResourceManager):UndertowResourceHandler = new UndertowResourceHandler(manager)
}