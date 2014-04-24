package se.chimps.cameltow.servlets

import java.io.File
import se.chimps.cameltow.exceptions.InvalidConfigurationException
import org.slf4j.Logger

/**
 * Created by meduzz on 24/04/14.
 */
trait Servlets {
  def logger:Logger

  def registerWar(warFile:File) = {
    if (!warFile.exists() || !warFile.isFile) {
      throw new InvalidConfigurationException("Warfile does not exists, or are not a file!")
    }

    // TODO
  }
}
