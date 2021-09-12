package org.flinklab.util

import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Assigns timestamps to ThingUpdate based on their internal timestamp and
  * emits watermarks with five seconds slack.
  */
class ThingTimeAssigner
    extends BoundedOutOfOrdernessTimestampExtractor[ThingUpdate](Time.seconds(5)) {

  /** Extracts timestamp from ThingUpdate. */
  override def extractTimestamp(r: ThingUpdate): Long = r.timestamp

}
