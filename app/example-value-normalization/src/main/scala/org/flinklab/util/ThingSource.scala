package org.flinklab.util

import java.util.Calendar

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

class ThingSource extends RichParallelSourceFunction[ThingUpdate] {

  var isRunning: Boolean = true

  override def run(srcCtx: SourceContext[ThingUpdate]): Unit = {

    val taskIdx = this.getRuntimeContext.getIndexOfThisSubtask

    while (isRunning) {

      val curTime = Calendar.getInstance.getTimeInMillis

      val curFTemp = (1 to 3).map {
        i => ("thing_" + (taskIdx * 10 + i), curTime, taskIdx * 100 + i)
      }

      curFTemp.foreach( t => srcCtx.collect(ThingUpdate(t._1, t._2, t._3)))

      Thread.sleep(1000)
    }

  }

  /** Cancels this SourceFunction. */
  override def cancel(): Unit = {
    isRunning = false
  }

}
