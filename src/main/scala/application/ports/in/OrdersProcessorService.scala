package eci.edu.co
package application.ports.in

import eci.edu.co.domain.OrdersPlacedMetricsPerMonth
import org.apache.spark.sql.DataFrame

trait OrdersProcessorService {
  def sendTotalOrdersArmiAgainstOthersPerDay(dataFrame: DataFrame): Unit

  def sendTotalOrdersArmiAgainstOthersPerMonth(dataFrame: DataFrame): Unit

  def sendTotalOrdersPlacedAgainstOthersPerDay(data: DataFrame): Unit

  def sendTotalOrdersPlacedAgainstOthersPerMonth(data: DataFrame): Unit

  def sendOrdersPlacedPerDayByCity(data: DataFrame): Unit

  def sendOrdersPlacedPerDayByStore(data: DataFrame): Unit
}
