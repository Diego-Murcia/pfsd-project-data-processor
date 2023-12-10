package eci.edu.co
package adapters.out

import application.ports.out.OrdersProcessorAdapter

import adapters.out.kafka.KafkaProducerServer
import domain.{ManagedOrdersPerDayByCity, ManagedOrdersPerDayByStore, OrdersPlacedAgainstNoPlacedPerDay, OrdersPlacedAgainstNoPlacedPerMonth, OrdersPlacedMetricsPerDay, OrdersPlacedMetricsPerMonth}
import org.apache.kafka.clients.producer.ProducerRecord

class OrdersProcessorAdapterImpl(kafkaProducerServer: KafkaProducerServer) extends OrdersProcessorAdapter {
  def sendTotalOrdersArmiAgainstOthersPerDay(data:  Array[OrdersPlacedMetricsPerDay]): Unit = {
    data.zipWithIndex.foreach { case (item, index) =>
      kafkaProducerServer.producer.send(new ProducerRecord[String, String]("data-processed-topic", s"armi-orders-vs-others-per-day-$index", item.toString))
    }
  }

  def sendTotalOrdersArmiAgainstOthersPerMonth(data: Array[OrdersPlacedMetricsPerMonth]): Unit = {
    data.zipWithIndex.foreach { case (item, index) =>
      kafkaProducerServer.producer.send(new ProducerRecord[String, String]("data-processed-topic", s"armi-orders-vs-others-per-month-$index", item.toString))
    }
  }

  def sendTotalOrdersPlacedAgainstOthersPerDay(data: Array[OrdersPlacedAgainstNoPlacedPerDay]): Unit = {
    data.zipWithIndex.foreach { case (item, index) =>
      kafkaProducerServer.producer.send(new ProducerRecord[String, String]("data-processed-topic", s"armi-orders-placed-per-day-$index", item.toString))
    }
  }

  def sendTotalOrdersPlacedAgainstOthersPerMonth(data: Array[OrdersPlacedAgainstNoPlacedPerMonth]): Unit = {
    data.zipWithIndex.foreach { case (item, index) =>
      kafkaProducerServer.producer.send(new ProducerRecord[String, String]("data-processed-topic", s"armi-orders-placed-per-month-$index", item.toString))
    }
  }

  def sendOrdersPlacedPerDayByCity(data: Array[ManagedOrdersPerDayByCity]): Unit = {
    data.zipWithIndex.foreach { case (item, index) =>
      kafkaProducerServer.producer.send(new ProducerRecord[String, String]("data-processed-topic", s"orders-placed-per-day-by-city-$index", item.toString))
    }
  }

  def sendOrdersPlacedPerDayByStore(data: Array[ManagedOrdersPerDayByStore]): Unit =
    data.zipWithIndex.foreach { case (item, index) =>
      kafkaProducerServer.producer.send(new ProducerRecord[String, String]("data-processed-topic", s"orders-placed-per-day-by-store-$index", item.toString))
    }
}
