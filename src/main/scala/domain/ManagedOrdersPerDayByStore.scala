package eci.edu.co
package domain

import java.time.LocalDate

case class ManagedOrdersPerDayByStore(date: LocalDate, city: String, storeId: String, storeName: String, orders: Long, ordersArmi: Long, percentage: Double)
