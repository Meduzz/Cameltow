package se.chimps.cameltow.servlets

import java.io.File
import se.chimps.cameltow.exceptions.InvalidConfigurationException
import org.slf4j.Logger
import javax.servlet.http.HttpServlet

/**
 * Created by meduzz on 24/04/14.
 */
trait Servlets {
  def logger:Logger

  def registerWar(warFile:File) = {
    if (!warFile.exists() || !warFile.isFile) {
      throw new InvalidConfigurationException("Warfile does not exists, or are not a file!")
    }

    // TODO implement
  }

  // Shortcut to Spring intialization for thoose layed in that direction.
  def registerServlet(servlet:HttpServlet) = {
    // TODO implement
  }
}
