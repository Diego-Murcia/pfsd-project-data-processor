package eci.edu.co
package adapters.out.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties

trait KafkaProducerServer {
  private val kafkaProps: Properties = {
    val props = new Properties()
    val bootstrapServers = sys.env.getOrElse("KAFKA_BOOTSTRAP_SERVERS", "localhost:9093")

    props.put("bootstrap.servers", bootstrapServers)
    props.put("key.serializer", classOf[StringSerializer].getName)
    props.put("value.serializer", classOf[StringSerializer].getName)

    props
  }

  def producer: KafkaProducer[String, String] = new KafkaProducer[String, String](kafkaProps)
}
