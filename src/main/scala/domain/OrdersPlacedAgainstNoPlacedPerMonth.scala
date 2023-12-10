package eci.edu.co
package domain

import org.joda.time.LocalDateTime

case class OrdersPlacedAgainstNoPlacedPerMonth(month: String, ordersPlaced: Long, ordersNoPlaced: Long)
