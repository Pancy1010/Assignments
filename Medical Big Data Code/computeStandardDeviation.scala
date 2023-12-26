package com.app
import org.apache.spark.sql.SparkSession

object computeStandardDeviation {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("test")
      .getOrCreate()

    spark.sparkContext.setLogLevel("warn")

    import spark.implicits._

    val sourceDf = spark
      .read
      .option("header", true)
      .csv("hdfs://localhost:8020/disease/disease.csv")
      .toDF("id", "height", "weight", "age", "disease", "medicine", "date")

    val formatData = sourceDf
      .filter(row => row.getString(0) != null)
      .map(row => {
        val hour = row.getString(6).split(" ")(1).split(":")(0)
        (row.getString(0).toInt, row.getString(1).toInt, row.getString(2).toInt, row.getString(3).toInt, row.getString(4), row.getString(5), row.getString(6), hour.toInt)
      })
      .toDF("id", "height", "weight", "age", "disease", "medicine", "date", "hour")

    val df = formatData
      .groupBy("hour")
      .count()
      .sort($"count".desc)

    import scala.collection.JavaConverters._

    // 提取 count 字段的值作为 Long 数组
    val countValues = df.select("count").rdd.map(_.getLong(0))

    // 计算 count 字段的正态分布参数
    val (mean, variance) = countValues.mean() -> countValues.variance()
    val stdDev = Math.sqrt(variance)

    // 输出结果
    println(s"Mean: $mean")
    println(s"Standard Deviation: $stdDev")

    spark.stop()
  }
}
