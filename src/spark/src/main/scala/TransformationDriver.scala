package com.foo.datainsights

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

    val args2 = (Consts.Zookeeper, "group1", Consts.TopicName, 1)
    val (zkQuorum, group, topics, numThreads) = args2

    val sparkConf = new SparkConf().setAppName("KafkaTransformer")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    ssc.checkpoint("checkpoint")
    val topicMap = topics.split(",").map((_, numThreads)).toMap

    val rows = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
    var headerRows = rows.map[ReportHeader](Serialization.read[ReportHeader])



    //val count = rows.countByWindow(Minutes(1), Minutes(1))
    //count.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
