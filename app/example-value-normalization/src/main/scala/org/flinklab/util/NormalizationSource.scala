package org.flinklab.util

import java.util.Calendar

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

import scala.util.Random

class NormalizationSource extends RichParallelSourceFunction[NormalizationUpdate] {

  var isRunning: Boolean = true

  override def run(srcCtx: SourceContext[NormalizationUpdate]): Unit = {

    // Ensure there is no normalization data to start with by having it arrive
    // later than the thing data by forcing a little bit of a delay.
    Thread.sleep(1000)

    val taskIdx = this.getRuntimeContext.getIndexOfThisSubtask

    // Limit the number of choices, so it is common to get the same choice,
    // which will lead to no downstream output.
    val min = 1
    val max = 2

    while (isRunning) {

      val curTime = Calendar.getInstance.getTimeInMillis

      val updates = (1 to 3).map {
        i => (taskIdx * 100 + i, curTime, new Random().nextInt(max - min + 1) + min)
      }

      updates.foreach( t => srcCtx.collect(NormalizationUpdate(t._1, t._2, t._3)))

      Thread.sleep(1000)
    }

  }

  override def cancel(): Unit = {
    isRunning = false
  }

}
