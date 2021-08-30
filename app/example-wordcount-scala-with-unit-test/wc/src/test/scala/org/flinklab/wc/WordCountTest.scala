import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.test.util.AbstractTestBase
import org.junit.Test
import org.junit.Assert._
import org.apache.flink.streaming.api.scala._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by zuoyuan on 2019/7/8.
  * Original code from here: https://github.com/tracholar/ml-homework-cz/blob/7e1628b7b4afa1f4290d51a9e30ffbc84cab8ff0/flink/tracholar/src/test/scala/com/tracholar/flink/demo/DemoTest.scala
  */

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

class WordCountTest extends AbstractTestBase{
	@Test
	def testMultiply = {
		val env = StreamExecutionEnvironment.getExecutionEnvironment
		env.setParallelism(1)

		env.fromElements(1L, 21L, 22L)
		    .map(new MultiplyByTwo)
		    .addSink(new CollectSink)

		env.execute()
		assertArrayEquals(Array(2L, 42L, 44L), CollectSink.values.toArray)
	}
}
