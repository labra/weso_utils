package es.weso.utils

import jline.console.ConsoleReader
import com.typesafe.scalalogging._
import jline.console.ConsoleReader

/**
  * Trait for debugging
  * 
  */
trait Debugging extends StrictLogging {

  val consoleReader = new ConsoleReader()
  
  def error(msg:String): Unit = logger.error(msg)
  def warn(msg:String): Unit = logger.warn(msg)
  def info(msg:String): Unit = logger.info(msg)
  def debug(msg:String): Unit = logger.debug(msg)
  
  /**
   * Injected read function
   * Usually: consoleReader.readCharacter()
   */
  def readFn(): String = {
    consoleReader.flush()
    val ch = consoleReader.readCharacter()
    ch.toChar.toString
  }
  
  /**
   * Show debug messages 
   */
  val showDebug: Boolean = false

  /**
   * Show debug messages 
   */
  val showInfo: Boolean = true
  
  /**
   * Stop after each debug message and ask the user to continue
   */
  var stopAfterShow: Boolean = false
  
  private def checkStop(forceStop: Boolean, action: => Unit): Unit = {
    if (forceStop) {
      stopAfterShow = true
    }
    if (stopAfterShow) {
      consoleReader.println("\nAction? (n = next, r = resume, q = quit):")
      val next = readFn()
      next.toUpperCase match {
        case "N" => return
        case "R" => {
          stopAfterShow = false
          return
        }
        case "Q" => System.exit(0)
        case c => {
          info(s"\nUnknown char: $c");
          action
        }
      }
    }    
  }

  def debugStep(msg: String, forceStop: Boolean = false) {
    if (showDebug || stopAfterShow) { 
      debug(msg) 
      checkStop(forceStop, debugStep(msg))
    }
  }

  def infoStep(msg: String, forceStop: Boolean = false) {
    if (showInfo || forceStop) { 
      info(msg) 
      checkStop(forceStop, infoStep(msg))
    }
  }
  
}

