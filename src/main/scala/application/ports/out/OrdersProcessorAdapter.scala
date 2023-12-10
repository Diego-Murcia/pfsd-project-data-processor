package eci.edu.co
package application.ports.out

import domain.{ManagedOrdersPerDayByCity, ManagedOrdersPerDayByStore, OrdersPlacedAgainstNoPlacedPerDay, OrdersPlacedAgainstNoPlacedPerMonth, OrdersPlacedMetricsPerDay, OrdersPlacedMetricsPerMonth}

trait OrdersProcessorAdapter {
  def sendTotalOrdersArmiAgainstOthersPerDay(data: Array[OrdersPlacedMetricsPerDay]): Unit
  def sendTotalOrdersArmiAgainstOthersPerMonth(data: Array[OrdersPlacedMetricsPerMonth]): Unit
  def sendTotalOrdersPlacedAgainstOthersPerDay(data: Array[OrdersPlacedAgainstNoPlacedPerDay]): Unit
  def sendTotalOrdersPlacedAgainstOthersPerMonth(data: Array[OrdersPlacedAgainstNoPlacedPerMonth]): Unit
  def sendOrdersPlacedPerDayByCity(data: Array[ManagedOrdersPerDayByCity]): Unit
  def sendOrdersPlacedPerDayByStore(data: Array[ManagedOrdersPerDayByStore]): Unit
}
