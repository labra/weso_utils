package es.weso.utils

import java.io.File

/**
 * Common utils on Files
 */
object FileUtils {

  /**
   * Get URI that represents a file. 
   * Code obtained from [[http://stackoverflow.com/questions/8323760/java-get-uri-from-filepath]]
   * 
   */
  def filePath2URI(path: String): String = {
    new File(path).toURI().toURL().toExternalForm()
  }
  

  /**
   * Write contents to a file
   * 
   * @param name name of the file
   * @param contents contents to write to the file
   */
  def writeFile(name: String, contents: String): Unit = {
    import java.nio.file._
    val path = Paths.get(name)
    Files.write(path, contents.getBytes)
  }

}