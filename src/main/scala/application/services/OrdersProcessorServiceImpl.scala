package eci.edu.co
package application.services

import application.ports.in.OrdersProcessorService

import eci.edu.co.application.ports.out.OrdersProcessorAdapter
import eci.edu.co.domain.{ManagedOrdersPerDayByCity, ManagedOrdersPerDayByStore, OrdersPlacedAgainstNoPlacedPerDay, OrdersPlacedAgainstNoPlacedPerMonth, OrdersPlacedMetricsPerDay, OrdersPlacedMetricsPerMonth}
import eci.edu.co.utilities.SparkSessionWrapper
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{concat_ws, count, date_format, regexp_extract, round, split, sum, to_date, unix_timestamp, when}

class OrdersProcessorServiceImpl(ordersProcessorAdapter: OrdersProcessorAdapter) extends OrdersProcessorService with SparkSessionWrapper {
  import spark.implicits._

  def sendTotalOrdersArmiAgainstOthersPerDay(dataFrame: DataFrame): Unit = {
    val data = dataFrame
      .filter(unix_timestamp($"create_date", "yyyy-MM-dd HH:mm:ss").isNotNull)
      .withColumn("date", to_date($"create_date"))
      .groupBy($"date")
      .agg(
        sum(when($"courier" === "APOYO_FTD", 1).otherwise(0)).as("total_orders_armi"),
        sum(when($"courier" !== "APOYO_FTD", 1).otherwise(0)).as("total_orders_others")
      )
      .select($"date", $"total_orders_armi".as("ordersArmi"), $"total_orders_others".as("ordersOthers"))
      .as[OrdersPlacedMetricsPerDay]
      .collect()

    ordersProcessorAdapter.sendTotalOrdersArmiAgainstOthersPerDay(data)
  }

  def sendTotalOrdersArmiAgainstOthersPerMonth(dataFrame: DataFrame): Unit = {
    val data = dataFrame
      .filter(unix_timestamp($"create_date", "yyyy-MM-dd HH:mm:ss").isNotNull)
      .withColumn("month", date_format($"create_date", "MMMM"))
      .groupBy($"month")
      .agg(
        sum(when($"courier" === "APOYO_FTD", 1).otherwise(0)).as("total_orders_armi"),
        sum(when($"courier" !== "APOYO_FTD", 1).otherwise(0)).as("total_orders_others")
      )
      .select($"month", $"total_orders_armi".as("ordersArmi"), $"total_orders_others".as("ordersOthers"))
      .as[OrdersPlacedMetricsPerMonth]
      .collect()

    ordersProcessorAdapter.sendTotalOrdersArmiAgainstOthersPerMonth(data);
  }

  def sendTotalOrdersPlacedAgainstOthersPerDay(dataFrame: DataFrame): Unit = {
    val data = dataFrame
      .filter(unix_timestamp($"create_date", "yyyy-MM-dd HH:mm:ss").isNotNull)
      .withColumn("date", to_date($"create_date"))
      .where($"courier" === "APOYO_FTD")
      .groupBy($"date")
      .agg(
        sum(when($"status_orden" === "FINALIZADA", 1).otherwise(0)).as("total_orders_placed"),
        sum(when($"status_orden" !== "FINALIZADA", 1).otherwise(0)).as("total_orders_no_placed")
      )
      .select($"date", $"total_orders_placed".as("ordersPlaced"), $"total_orders_no_placed".as("ordersNoPlaced"))
      .as[OrdersPlacedAgainstNoPlacedPerDay]
      .collect()

    ordersProcessorAdapter.sendTotalOrdersPlacedAgainstOthersPerDay(data)
  }

  def sendTotalOrdersPlacedAgainstOthersPerMonth(dataFrame: DataFrame): Unit = {
    val data = dataFrame
      .filter(unix_timestamp($"create_date", "yyyy-MM-dd HH:mm:ss").isNotNull)
      .withColumn("month", date_format($"create_date", "MMMM"))
      .where($"courier" === "APOYO_FTD")
      .groupBy($"month")
      .agg(
        sum(when($"status_orden" === "FINALIZADA", 1).otherwise(0)).as("total_orders_placed"),
        sum(when($"status_orden" !== "FINALIZADA", 1).otherwise(0)).as("total_orders_no_placed")
      )
      .select($"month", $"total_orders_placed".as("ordersPlaced"), $"total_orders_no_placed".as("ordersNoPlaced"))
      .as[OrdersPlacedAgainstNoPlacedPerMonth]
      .collect()

    ordersProcessorAdapter.sendTotalOrdersPlacedAgainstOthersPerMonth(data)
  }

  def sendOrdersPlacedPerDayByCity(dataFrame: DataFrame): Unit = {
    val data = dataFrame
      .filter(unix_timestamp($"create_date", "yyyy-MM-dd HH:mm:ss").isNotNull)
      .withColumn("date", to_date($"create_date"))
      .groupBy($"date", $"order_city")
      .agg(
        count($"order_id").as("total_orders"),
        sum(when($"courier" === "APOYO_FTD", 1).otherwise(0)).as("total_orders_armi")
      )
      .withColumn("managed_percentage", round(($"total_orders_armi" / $"total_orders") * 100, 2))
      .select($"date", $"order_city".as("city"), $"total_orders".as("orders"), $"total_orders_armi".as("ordersArmi"), $"managed_percentage".as("percentage"))
      .as[ManagedOrdersPerDayByCity]
      .collect()

    ordersProcessorAdapter.sendOrdersPlacedPerDayByCity(data)
  }

  def sendOrdersPlacedPerDayByStore(dataFrame: DataFrame): Unit = {
    val data = dataFrame
      .filter(unix_timestamp($"create_date", "yyyy-MM-dd HH:mm:ss").isNotNull)
      .filter($"closer_store_id".isNotNull && regexp_extract($"closer_store_id", "^[0-9]+$", 0) =!= "")
      .withColumn("date", to_date($"create_date"))
      .withColumn("store_info", concat_ws("-", $"order_city", $"closer_store_id", $"closer_store"))
      .groupBy($"date", $"store_info")
      .agg(
        count($"order_id").as("total_orders"),
        sum(when($"courier" === "APOYO_FTD", 1).otherwise(0)).as("total_orders_armi")
      )
      .withColumn("split_store_info", split($"store_info", "-"))
      .withColumn("city", $"split_store_info".getItem(0))
      .withColumn("store_id", $"split_store_info".getItem(1))
      .withColumn("store_name", $"split_store_info".getItem(2))
      .withColumn("managed_percentage", round(($"total_orders_armi" / $"total_orders") * 100, 2))
      .select($"date", $"city", $"store_id".as("storeId"), $"store_name".as("storeName"), $"total_orders".as("orders"), $"total_orders_armi".as("ordersArmi"), $"managed_percentage".as("percentage"))
      .as[ManagedOrdersPerDayByStore]
      .collect()

    ordersProcessorAdapter.sendOrdersPlacedPerDayByStore(data)
  }
}