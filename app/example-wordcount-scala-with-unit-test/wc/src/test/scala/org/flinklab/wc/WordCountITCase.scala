import org.flinklab.wc.WordCount
import org.apache.flink.test.util.{AbstractTestBase, TestBaseUtils}
import org.junit.Test

/**
 * Integration test for WordCount
 */
class WordCountITCaseTest extends AbstractTestBase {

  @Test
  def testWordCount(): Unit = {
    val inputStr: String = "do be"
    val expected: String = "be 1\ndo 1"
  
    val textPath = createTempFile("text.txt", inputStr)
    val resultPath = getTempDirPath("result")

    WordCount.main(Array(
      "--input", textPath,
      "--output", resultPath
    ))

    TestBaseUtils.compareResultsByLinesInMemory(
      expected,
      resultPath)
  }

}
