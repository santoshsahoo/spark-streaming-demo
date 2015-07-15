package com.foo.datainsights

import _root_.kafka.serializer.StringDecoder
import com.redis.RedisClient
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.{Logging, SparkConf}

object TransformationDriver extends Logging {

  def main(args: Array[String]) {

    val redisChannelNameCounts = "reportCounts"
    val redisChannelNameFraudAlert = "reportFraudAlert"
    val checkpointDirectory = "/tmp/spark/checkpoint"

    val sparkConf = new SparkConf().setAppName("ReportingTransformer")

    def createNewSSC(): StreamingContext = {
      val ssc = new StreamingContext(sparkConf, Seconds(5))
      ssc.checkpoint(checkpointDirectory)
      ssc
    }

    val ssc = createNewSSC() //StreamingContext.getOrCreate(checkpointDirectory, createNewSSC _)

    val kafkaParams = Map("metadata.broker.list" -> Consts.BrokerNames)
    val topics = Set(Consts.REPORTS_TopicName)

    val kafkaDStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)

    val reportDs = kafkaDStream.map(entry => entry.toString())
    val reports = reportDs.map(csv => ReportHeader.parseCsv(csv))

    val fraudDs = reports.filter(report => report != null && report.getTotal > 49000)

    fraudDs.foreachRDD(
      rdd => {
        if (!rdd.isEmpty()) {
          val redis = new RedisClient("localhost", 6379)
          val id = rdd.first().getReportId.toString
          redis.publish(redisChannelNameFraudAlert, id)
          redis.disconnect
        }
      })


    val countDS = reports.countByWindow(Minutes(1), Seconds(10))

    countDS.foreachRDD {
      rdd => {
        if (!rdd.isEmpty()) {
          val redis = new RedisClient("localhost", 6379)
          val count: String = rdd.first().toString
          redis.publish(redisChannelNameCounts, count)

          logTrace("The count is %s".format(count))
          logTrace("-----------------------------------------------------------------")
          redis.disconnect
        }
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
