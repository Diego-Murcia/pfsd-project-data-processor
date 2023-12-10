package eci.edu.co
package domain

import org.joda.time.LocalDateTime

case class OrdersPlacedMetricsPerMonth(month: String, ordersArmi: Long, ordersOthers: Long)
