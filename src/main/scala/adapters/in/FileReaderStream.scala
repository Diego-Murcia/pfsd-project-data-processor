package eci.edu.co
package adapters.in

import eci.edu.co.adapters.out.kafka.KafkaProducerServer
import eci.edu.co.application.ports.in.OrdersProcessorService
import eci.edu.co.utilities.SparkSessionWrapper

import java.nio.file.{FileSystems, Paths, StandardWatchEventKinds}

class FileReaderStream(ordersProcessorService: OrdersProcessorService, kafkaProducerServer: KafkaProducerServer) extends SparkSessionWrapper {
  def run(): Unit = {
    val directoryPath = "C:\\Users\\dmurc\\Desktop\\pfsd-project-bucket"
    val path = FileSystems.getDefault.getPath(directoryPath)
    val watchService = FileSystems.getDefault.newWatchService()

    path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE)

    while (true) {
      val key = watchService.take()

      key.pollEvents().forEach { event =>
        val fileName = event.context().toString
        val filePath = Paths.get(directoryPath, fileName).toString
        val dataFrame = spark.read.option("header", "true").csv(filePath)

        ordersProcessorService.sendTotalOrdersArmiAgainstOthersPerDay(dataFrame)
        ordersProcessorService.sendTotalOrdersArmiAgainstOthersPerMonth(dataFrame)
        ordersProcessorService.sendTotalOrdersPlacedAgainstOthersPerDay(dataFrame)
        ordersProcessorService.sendTotalOrdersPlacedAgainstOthersPerMonth(dataFrame)
        ordersProcessorService.sendOrdersPlacedPerDayByCity(dataFrame)
        ordersProcessorService.sendOrdersPlacedPerDayByStore(dataFrame)
      }

      key.reset()
    }

    kafkaProducerServer.producer.close()
  }
}
