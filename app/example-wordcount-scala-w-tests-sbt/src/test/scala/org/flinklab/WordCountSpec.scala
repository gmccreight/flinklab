import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction

import org.apache.flink.streaming.api.scala._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class MultiplyByTwo extends MapFunction[Long, Long] {
	override def map(value: Long): Long = {
		value * 2
	}
}

object CollectSink {
	// must be static
	val values: ListBuffer[Long] = mutable.ListBuffer.empty[Long]
}

class CollectSink extends SinkFunction[Long] {
	override def invoke(value :Long) = {
		synchronized {
			CollectSink.values.append(value)
		}
	}
}

class WordCountSpec extends FlatSpec with ShouldMatchers {

  "The stream processing" should "multiply things by two" in {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
		env.setParallelism(1)
    // env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
		env.fromElements(1L, 21L, 22L)
		    .map(new MultiplyByTwo)
		    .addSink(new CollectSink)

		env.execute()
    CollectSink.values.toArray should be(Array(2L, 42L, 44L))
	}

}
