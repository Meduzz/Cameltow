package se.chimps.cameltow.modules

import java.io.File
import se.chimps.cameltow.exceptions.InvalidConfigurationException
import javax.servlet.http.HttpServlet
import se.chimps.cameltow.Cameltow

/**
 * Created by meduzz on 24/04/14.
 */
trait Servlets { self:Cameltow =>

  def registerWar(warFile:File) = {
    if (!warFile.exists() || !warFile.isFile) {
      throw new InvalidConfigurationException("Warfile does not exists, or are not a file!")
    }

    // TODO implement
  }

  def registerServlet(path:String, servlet:HttpServlet) = {
    // TODO implement
  }

  // TODO support @Servlet annotation scanning of classes and shite?
}
