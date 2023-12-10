package eci.edu.co
package domain

import java.time.LocalDate

case class ManagedOrdersPerDayByCity(date: LocalDate, city: String,  orders: Long, ordersArmi: Long, percentage: Double)
