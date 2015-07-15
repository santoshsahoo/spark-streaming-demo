/**
 * Created by ssahoo on 6/10/15.
 */

package com.foo.datainsights;

import java.util.{Properties, UUID}
import java.io._
import kafka.common._
import kafka.message._
import kafka.producer.{ProducerConfig, Producer}
import kafka.serializer._
import java.util.Properties

object KafkaHelper {
  def put(s:String) ={

    val props = new Properties()
    props.put("producer.type", "async")
    props.put("metadata.broker.list", Consts.BrokerNames)
    props.put("client.id", "test client")

    val producer = new Producer[AnyRef, AnyRef](new ProducerConfig(props))
  }
}
