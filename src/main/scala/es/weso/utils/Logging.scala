package es.weso.utils

import com.typesafe.scalalogging._

/**
 * Trait for things that can log
 * @note We recommend to use the `Debugging` trait instead of this one  
 */
trait Logging extends LazyLogging {
  
  
  def info(msg: String) {
    logger.info(msg)
  }
  
  def debug(msg: String) {
    logger.debug(msg)
  }

}

