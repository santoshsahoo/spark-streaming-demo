package com.concur.datainsights

import org.apache.kafka.clients.producer.{ProducerConfig, KafkaProducer, ProducerRecord}
import org.apache.spark.{SparkConf, Logging}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.log4j.{Level, Logger}

object Transformer extends Logging {
  def main(args: Array[String]) {
    val Array("localhost:2181", "group1", "expense.reports", "4") = args

    val sparkConf = new SparkConf().setAppName("Transformer")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    ssc.checkpoint("checkpoint")

    ssc.start()
  }
}
