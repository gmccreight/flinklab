package org.flinklab.sic

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.common.state.{ValueState,ValueStateDescriptor,MapState,MapStateDescriptor}
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.apache.flink.util.Collector

object Demo {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment

    val transactions: DataStream[JsonNode] =
      env.addSource(new TransactionsSource("/tmp/transactions", 100))

    // timestamp - from_account - amount_total
    val debits: DataStream[(Long, Long, Double)] = transactions
      .map(new ToDebit())
      .keyBy(new GetAccount())
      .process(new ProcessTransaction())
    debits
      .writeAsText("/tmp/debits", org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE)
      .setParallelism(1)

    // timestamp - to_account - amount_total
    val credits: DataStream[(Long, Long, Double)] = transactions
      .map(new ToCredit())
      .keyBy(new GetAccount())
      .process(new ProcessTransaction())
    credits
      .writeAsText("/tmp/credits", org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE)
      .setParallelism(1)

    // timestamp - account - balance
    val balance: DataStream[(Long, Long, Double)] = debits
      .keyBy(new GetAccount())
      .union(credits)
      .keyBy(new GetAccount())
      .process(new ProcessTransaction())
    balance
      .writeAsText("/tmp/balance", org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE)
      .setParallelism(1)

    val total: DataStream[(Long, Long, Double)] = balance
      .keyBy(new GetOneKey())
      .process(new ProcessTotal())
      .setParallelism(1)
    total
      .writeAsText("/tmp/total", org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE)
      .setParallelism(1)

    env.execute("Internal Consistency Demo - Scala")
  }

  class ProcessTransaction extends KeyedProcessFunction[Long, (Long, Long, Double), (Long, Long, Double)] {

    // timestamp -> amount
    private var balance: MapState[Long, Double] = _

    override def open(parameters: Configuration): Unit = {
      val taskDescriptor: MapStateDescriptor[Long, Double] = new MapStateDescriptor[Long, Double]("amountState", classOf[Long], classOf[Double])
      balance = getRuntimeContext.getMapState(taskDescriptor)
    }

    override def processElement(
        in: (Long, Long, Double),
        ctx: KeyedProcessFunction[Long, (Long, Long, Double), (Long, Long, Double)]#Context,
        collector: Collector[(Long, Long, Double)]): Unit = {

      // add update map and set timer
      val timestamp: Long = ctx.timestamp()
      var amount: Double = 0.0
      amount =
        if (balance.contains(timestamp)) balance.get(timestamp) + in._3
        else in._3
      balance.put(timestamp, amount)
      ctx.timerService().registerEventTimeTimer(ctx.timestamp())
    }

    override def onTimer(timestamp: Long,
                         ctx: KeyedProcessFunction[Long, (Long, Long, Double), (Long, Long, Double)]#OnTimerContext,
                         out: Collector[(Long, Long, Double)]): Unit = {
      // emit updates
      out.collect(
        (timestamp, ctx.getCurrentKey, balance.get(timestamp)))

      // clean up state
      balance.remove(timestamp)
    }

  }

  class ProcessTotal extends KeyedProcessFunction[Long, (Long, Long, Double), (Long, Long, Double)] {

    private var total: ValueState[Double] = _

    override def open(parameters: Configuration): Unit = {
      val taskDescriptor: ValueStateDescriptor[Double] =
        new ValueStateDescriptor[Double]("totalState", classOf[Double])
      total = getRuntimeContext.getState(taskDescriptor)
    }

    override def processElement(
        in: (Long, Long, Double),
        ctx: KeyedProcessFunction[Long, (Long, Long, Double), (Long, Long, Double)]#Context,
        out: Collector[(Long, Long, Double)]): Unit = {
      var amount: Double = 0
      if (Option(total.value()) != None) {
        amount = total.value()
      }
      amount += in._3
      total.update(amount)
      ctx.timerService().registerEventTimeTimer(in._1)
    }

    override def onTimer(timestamp: Long,
                         ctx: KeyedProcessFunction[Long, (Long, Long, Double), (Long, Long, Double)]#OnTimerContext,
                         out: Collector[(Long, Long, Double)]): Unit = {
      // emit updates
      out.collect((timestamp, ctx.getCurrentKey, total.value()))
    }

  }
  class ToDebit extends MapFunction[JsonNode, (Long, Long, Double)] {
    override def map(in: JsonNode): (Long, Long, Double) = (in.get("ts").asLong(), in.get("from_account").asLong(), -in.get("amount").asDouble())
  }

  class ToCredit extends MapFunction[JsonNode, (Long, Long, Double)] {
    override def map(in: JsonNode): (Long, Long, Double) = (in.get("ts").asLong(), in.get("to_account").asLong(), in.get("amount").asDouble())
  }

  class GetAccount extends KeySelector[(Long, Long, Double), Long] {
    override def getKey(in: (Long, Long, Double)): Long = in._2
  }

  class GetOneKey extends KeySelector[(Long, Long, Double), Long] {
    override def getKey(in: (Long, Long, Double)): Long = 0L
  }
}
