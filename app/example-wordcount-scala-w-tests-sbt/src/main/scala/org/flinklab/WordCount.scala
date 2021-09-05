package org.flinklab

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._

/**
 * Implements the "WordCount" program that computes a simple word occurrence histogram
 * over text files. 
 *
 * The input is a plain text file with lines separated by newline characters.
 *
 * Usage:
 * {{{
 *   WordCount --input <path> --output <path>
 * }}}
 *
 */
object WordCount {

  def main(args: Array[String]) {

    val params: ParameterTool = ParameterTool.fromArgs(args)

    // set up execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment

    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)
    val text =
      if (params.has("input")) {
        env.readTextFile(params.get("input"))
      } else {
        println("Executing WordCount example with default input data set.")
        println("Use --input to specify file input.")

        val words = Array(
          "To be, or not to be,--that is the question:--",
          "Whether 'tis nobler in the mind to suffer",
          "The slings and arrows of outrageous fortune",
          "Or to take arms against a sea of troubles,"
        )
        env.fromCollection(words)
      }

    val counts = text.flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
      .map { (_, 1) }
      .groupBy(0)
      .sum(1)

    if (params.has("output")) {
      counts.writeAsCsv(params.get("output"), "\n", " ")
      env.execute("Scala WordCount Example")
    } else {
      println("Printing result to stdout. Use --output to specify output path.")
      counts.print()
    }

  }
}
