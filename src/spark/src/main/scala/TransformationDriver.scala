package com.foo.datainsights

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.{Logging, SparkConf}

object TransformationDriver extends Logging {
  def main(args: Array[String]) {
    val args2 = ("localhost:2181/expensekafka", "group1", "expense.reports", "2")
    val (zkQuorum, group, topics, numThreads) = args2

    val sparkConf = new SparkConf().setAppName("KafkaTransformer")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    ssc.checkpoint("checkpoint")
    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val row = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)

    val count = row.countByWindow(Minutes(1), Seconds(2))

    count.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
