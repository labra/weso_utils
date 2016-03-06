package es.weso.utils

/**
 * Some utilities for Performance measurement
 * 
 */
object PerformanceUtils {

  /**
   * Executes an action and prints the elapsed time
   */
  def time[A](a: => A) = {
    val now = System.nanoTime
    val result = a
    val micros = (System.nanoTime - now) / 1000
    println("%d microseconds".format(micros))
    result
  }

  def getTimeNow(): Long = System.nanoTime

  def getTimeFrom(from: Long): Long = (System.nanoTime - from) / 1000

  def showTime(micros: Long): Unit = {
    println("** %d microseconds".format(micros))
  }

  /**
   * Shows the runtime memory.
   * See [[http://alvinalexander.com/scala/how-show-memory-ram-use-scala-application-used-free-total-max]]
   * 
   */
  def showRuntimeMemory(runtime: Runtime): Unit = {
    val mb = 1024 * 1024
    println("** Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb)
    println("** Free Memory:  " + runtime.freeMemory / mb)
    println("** Total Memory: " + runtime.totalMemory / mb)
    println("** Max Memory:   " + runtime.maxMemory / mb)
  }

}