package eci.edu.co

import adapters.in.FileReaderStream

import eci.edu.co.adapters.out.OrdersProcessorAdapterImpl
import eci.edu.co.adapters.out.kafka.KafkaProducerServer
import eci.edu.co.application.ports.in.OrdersProcessorService
import eci.edu.co.application.ports.out.OrdersProcessorAdapter
import eci.edu.co.application.services.OrdersProcessorServiceImpl

object Main extends App {
  val kafkaProducerServer: KafkaProducerServer = new KafkaProducerServer {}
  val ordersProcessorAdapter: OrdersProcessorAdapter = new OrdersProcessorAdapterImpl(kafkaProducerServer)
  val ordersProcessorService: OrdersProcessorService = new OrdersProcessorServiceImpl(ordersProcessorAdapter)
  val fileReaderStream: FileReaderStream = new FileReaderStream(ordersProcessorService, kafkaProducerServer)

  fileReaderStream.run()
}
