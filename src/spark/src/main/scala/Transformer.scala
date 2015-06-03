package com.foo.datainsights

import org.apache.kafka.clients.producer.{ProducerConfig, KafkaProducer, ProducerRecord}
import org.apache.spark.{SparkConf, Logging}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.log4j.{Level, Logger}

object Transformer extends Logging {
  def main(args: Array[String]) {
    val args2 = Array("localhost:2181", "group1", "expense.reports", "2")
    val Array(zkQuorum, group, topics, numThreads) = args

    val sparkConf = new SparkConf().setAppName("KafkaTransformer")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    ssc.checkpoint("checkpoint")
    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val rows = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)

    ssc.start()
  }
}
