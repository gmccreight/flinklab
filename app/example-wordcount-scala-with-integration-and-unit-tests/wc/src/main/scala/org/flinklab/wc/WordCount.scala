package org.flinklab.wc

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
/* import org.flinklab.wc.WordCountData */

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
 * If no parameters are provided, the program is run with default data from
 * [[org.flinklab.wc.WordCountData]]
 *
 * This example shows how to:
 *
 *   - write a simple Flink program.
 *   - use Tuple data types.
 *   - write and use user-defined functions.
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
          "Or to take arms against a sea of troubles,",
          "And by opposing end them?--To die,--to sleep,--",
          "No more; and by a sleep to say we end",
          "The heartache, and the thousand natural shocks",
          "That flesh is heir to,--'tis a consummation",
          "Devoutly to be wish'd. To die,--to sleep;--",
          "To sleep! perchance to dream:--ay, there's the rub;",
          "For in that sleep of death what dreams may come,",
          "When we have shuffled off this mortal coil,",
          "Must give us pause: there's the respect",
          "That makes calamity of so long life;",
          "For who would bear the whips and scorns of time,",
          "The oppressor's wrong, the proud man's contumely,",
          "The pangs of despis'd love, the law's delay,",
          "The insolence of office, and the spurns",
          "That patient merit of the unworthy takes,",
          "When he himself might his quietus make",
          "With a bare bodkin? who would these fardels bear,",
          "To grunt and sweat under a weary life,",
          "But that the dread of something after death,--",
          "The undiscover'd country, from whose bourn",
          "No traveller returns,--puzzles the will,",
          "And makes us rather bear those ills we have",
          "Than fly to others that we know not of?",
          "Thus conscience does make cowards of us all;",
          "And thus the native hue of resolution",
          "Is sicklied o'er with the pale cast of thought;",
          "And enterprises of great pith and moment,",
          "With this regard, their currents turn awry,",
          "And lose the name of action.--Soft you now!",
          "The fair Ophelia!--Nymph, in thy orisons",
          "Be all my sins remember'd."
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
