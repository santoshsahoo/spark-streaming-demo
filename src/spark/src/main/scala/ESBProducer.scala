package com.foo.datainsights

import java.util.{Date, HashMap}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.json4s.ShortTypeHints
import org.json4s.native.Serialization

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

    // Zookeeper connection properties
    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Consts.BrokerNames)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    var rptId: Int = 1

    // Send some messages
    while (true) {

      val entityId = Any.int(20000)
      val rpt = new ReportHeader(
        entityId, rptId, "Expense: " + Any.alphaString(200), new Date(), "user" + Any.int(2000), Any.int(50000).toLong)
      //        (1 to Any.int(10)).foreach { entryId =>
      //          val entry = ReportEntry(entityId, rptId, entryId, Any.alphaString(50), 200, 'C')
      //        }

      val rptKey = "%d:%d".format(entityId, rptId)
      val rptJson = rpt.toCsv

      val message = new ProducerRecord[String, String](Consts.REPORTS_TopicName, rptKey, rptJson)
      producer.send(message)

      rptId += 1

      Thread.sleep(Any.int(20))
    }
  }
}
