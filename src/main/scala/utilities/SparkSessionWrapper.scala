package eci.edu.co
package utilities

import org.apache.spark.sql.SparkSession

trait SparkSessionWrapper extends Serializable {
  lazy val spark: SparkSession = {
    SparkSession
      .builder()
      .appName("Data Processor")
      .master("local[*]")
      .getOrCreate()
  }
}
