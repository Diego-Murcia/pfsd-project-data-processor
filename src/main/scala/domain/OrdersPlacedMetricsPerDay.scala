package eci.edu.co
package domain

import java.time.LocalDate

case class OrdersPlacedMetricsPerDay(date: LocalDate, ordersArmi: Long, ordersOthers: Long)
