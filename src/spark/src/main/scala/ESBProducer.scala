package com.foo.datainsights

import java.util.{Date, HashMap}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.json4s._
import org.json4s.native.Serialization

import scala.util.Random

object ESBProducer {

  def main(args: Array[String]) {
    implicit val formats = Serialization.formats(
      ShortTypeHints(
        List(
          classOf[ReportHeader],
          classOf[ReportEntry]
        )
      )
    )

    val (brokers, topic, messagesPerSec) = (Consts.BrokerName, Consts.TopicName, 10)

    // Zookeeper connection properties
    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    // Send some messages
    while (true) {
      (1 to messagesPerSec).foreach { rptId =>
        val entityId = DataGenerator.int(20000)
        val rpt = ReportHeader(entityId, rptId, DataGenerator.string(20), new Date(), "user1")
        (1 to DataGenerator.int(10)).foreach { entryId =>
          val entry = ReportEntry(entityId, rptId, entryId, DataGenerator.string(20), 200, 'C')
        }

        val rptKey = "%d:%d".format(entityId, rptId)
        val rptJson = Serialization.write(rpt)

        val message = new ProducerRecord[String, String](topic, rptKey, rptJson)
        producer.send(message)
      }

      Thread.sleep(DataGenerator.int(500))
    }
  }
}
