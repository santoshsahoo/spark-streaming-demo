package com.foo.datainsights

import java.util.HashMap

import org.apache.kafka.clients.producer.{ProducerRecord, KafkaProducer, ProducerConfig}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.{Logging, SparkConf}
import org.json4s.ShortTypeHints
import org.json4s.native.Serialization

object TransformationDriver extends Logging {

  def main(args: Array[String]) {
    implicit val formats = Serialization.formats(
      ShortTypeHints(
        List(
          classOf[ReportHeader],
          classOf[ReportEntry]
        )
      )
    )


    val (brokers, topic, messagesPerSec) = (Consts.BrokerName, Consts.TopicName_Count, 10)

    // Zookeeper connection properties
    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    val args2 = (Consts.Zookeeper, "group1", Consts.TopicName, 1)
    val (zkQuorum, group, topics, numThreads) = args2

    val sparkConf = new SparkConf().setAppName("KafkaTransformer")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    ssc.checkpoint("checkpoint")
    val topicMap = topics.split(",").map((_, numThreads)).toMap

    val rows = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
    //var headerRows = rows.map[ReportHeader](Serialization.read[ReportHeader])

    val count = rows.countByWindow(Minutes(1), Seconds(10))

    var num = 0L

    count.foreachRDD { rdd => num += rdd.count() }
    count.print()

    println(num)

    val message = new ProducerRecord[String, String](topic, num.toString(), num.toString())
    producer.send(message)

    ssc.start()
    ssc.awaitTermination()
  }
}
