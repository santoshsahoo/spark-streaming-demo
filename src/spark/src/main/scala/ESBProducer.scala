package com.foo.datainsights

import java.util.HashMap
import java.util.Date
import kafka.utils.Json
import org.apache.ivy.util.DateUtil
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import scala.util.Random

object ESBProducer {

  def main(args: Array[String]) {
    val (brokers, topic, messagesPerSec) = ("localhost:9092", "expense.reports", 10000)

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
      val entityId = 1
      (1 to messagesPerSec).foreach { rptId =>
        val rpt = ReportHeader(entityId, rptId, "First report", DateUtil.parse("01/02/2014"), "user1")
        (1 to Random.nextInt(20)).foreach { entryId =>
          val entry = ReportEntry(entityId, rptId, entryId, "First report", 200, 'C')
        }

        val rptKey = "%d:%d".format(entityId, rptId)
        val rptVal = Json.encode(rpt)

        val message = new ProducerRecord[String, String](topic, rptKey, rptVal)
        producer.send(message)
      }

      Thread.sleep(100)
    }
  }
}

