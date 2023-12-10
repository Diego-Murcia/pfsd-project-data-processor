package eci.edu.co
package domain

import java.time.LocalDate

case class OrdersPlacedAgainstNoPlacedPerDay(date: LocalDate, ordersPlaced: Long, ordersNoPlaced: Long)
