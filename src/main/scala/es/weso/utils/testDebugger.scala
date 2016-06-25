package es.weso.utils
import jline.console.ConsoleReader

object testDebugger extends App with Debugging {
  override val showDebug = true
  override val showInfo = true
  stopAfterShow = false
  override def info(msg:String): Unit = System.out.println(msg)
  override def debug(msg:String): Unit = System.out.println("Debug:" + msg)
  override def error(msg:String): Unit = System.out.println("Error:" + msg)
  override def warn(msg:String): Unit = System.out.println("Warn:" + msg)

  debugStep("Hi!")
  debugStep("Hi 2!")
  debugStep("stopping", true)
  debugStep("next")
  debugStep("next 2")
  
}

object testDebuggerWithLog extends App with Debugging {
  override val showDebug = true
  debugStep("Hi!")
  debugStep("Hi 2!")
  debugStep("stopping", true)
  debugStep("next")
  debugStep("next 2")
}
