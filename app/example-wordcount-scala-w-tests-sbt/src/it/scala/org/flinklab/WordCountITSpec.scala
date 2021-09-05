import org.flinklab.WordCount

import org.scalatest.FlatSpec
import org.apache.flink.test.util.TestBaseUtils

import java.io.{File, PrintWriter}

class WordCountItSpec extends FlatSpec {

  "The integration test" should "work" in {
    val inputStr: String = "do be"
    val expected: String = "be 1\ndo 1"
  
    val inputFile = myCreateTempFile()
    val inputPath = inputFile.getAbsolutePath()
    writeToFile(inputFile, inputStr)

    val resultFileToDir = myCreateTempFile()
    resultFileToDir.delete()
    resultFileToDir.mkdir()
    val resultDirUriString = resultFileToDir.toURI().toString()

    WordCount.main(Array(
      "--input", inputPath,
      "--output", resultDirUriString
    ))

    TestBaseUtils.compareResultsByLinesInMemory(expected, resultDirUriString)
	}

  // From https://gist.github.com/malcolmgreaves/47a1ac470cd60cffe72ddcf1ea7b7df0
  /** Creates a temporary file, writes the input string to the file, and the file handle.
    *
    * NOTE: This funciton uses the createTempFile function from the File class. The prefix and
    * suffix must be at least 3 characters long, otherwise this function throws an
    * IllegalArgumentException.
    */
  def myCreateTempFile(prefix: Option[String] = None,
                      suffix: Option[String] = None): File = {
    val tempFi = File.createTempFile(prefix.getOrElse("prefix-"),
                                     suffix.getOrElse("-suffix"))
    tempFi.deleteOnExit()
    tempFi
  }

  def writeToFile(tempFi: File, contents: String) {
    new PrintWriter(tempFi) {
      // Any statements inside the body of a class in scala are executed on construction.
      // Therefore, the following try-finally block is executed immediately as we're creating
      // a standard PrinterWriter (with its implementation) and then using it.
      // Alternatively, we could have created the PrintWriter, assigned it a name, 
      // then called .write() and .close() on it. Here, we're simply opting for a terser representation.
      try {
        write(contents)
      } finally {
        close()
      }
    }
  }

}
