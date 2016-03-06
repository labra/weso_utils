package es.weso.utils

import org.slf4j._
import org.apache.log4j.LogManager
import org.apache.log4j.Level

/**
 * Represents things that can be verbose
 * At this moment, it is implemented on top of Logging. 
 *  
 */
trait Verbosity extends Logging {

  var isVerbose = true
  
  /**
   * Shows an info message 
   */
  def verbose(msg: String): Unit = {
    if (isVerbose) {
      log.info(msg)
    }
  }
  
  /**
   * Sets verbosity level
   */
  def setVerbosity(verbosity: Boolean): Unit = {
    isVerbose = verbosity
    if (isVerbose) {
      setInfo()
    } else {
      setError()
    }
  }
  
}