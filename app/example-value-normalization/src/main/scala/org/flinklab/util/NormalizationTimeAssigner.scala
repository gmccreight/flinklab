package org.flinklab.util

import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Assigns timestamps to NormalizationUpdate based on their internal timestamp and
  * emits watermarks with five seconds slack.
  */
class NormalizationTimeAssigner
    extends BoundedOutOfOrdernessTimestampExtractor[NormalizationUpdate](Time.seconds(5)) {

  /** Extracts timestamp from NormalizationUpdate. */
  override def extractTimestamp(r: NormalizationUpdate): Long = r.timestamp

}
