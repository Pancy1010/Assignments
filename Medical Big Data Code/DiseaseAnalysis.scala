package com.app
import org.apache.spark.sql.{SaveMode, SparkSession}

/*
|-- id: integer (nullable = false)
|-- height: integer (nullable = false)
|-- weight: integer (nullable = false)
|-- age: integer (nullable = false)
|-- disease: string (nullable = true)
|-- medicine: string (nullable = true)
|-- date: string (nullable = true)
|-- hour: integer (nullable = false)
*/

object DiseaseAnalysis {
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

    sourceDf.show()

    val formatData = sourceDf
      .filter(row => row.getString(0) != null)
      .map(row => {
        val hour = row.getString(6).split(" ")(1).split(":")(0)
        (row.getString(0).toInt, row.getString(1).toInt, row.getString(2).toInt, row.getString(3).toInt, row.getString(4), row.getString(5), row.getString(6), hour.toInt)
      })
      .toDF("id", "height", "weight", "age", "disease", "medicine", "date", "hour")

    val singleHourAskCount = formatData
      .groupBy("hour")
      .count()
      .sort($"count".desc)

    singleHourAskCount.show()
    singleHourAskCount
      .coalesce(1)
      .write
      .format("csv")
      .mode(SaveMode.Overwrite)
      .save("hdfs://localhost:8020/disease/singleHourAskCount")

    val diseaseCount = formatData
      .groupBy("disease")
      .count()
      .sort($"count".desc)
    diseaseCount.show()
    diseaseCount
    .coalesce(1)
      .write
      .format("csv")
      .mode(SaveMode.Overwrite)
      .save("hdfs://localhost:8020/disease/diseaseCount")

    val medicineCount = formatData
      .groupBy("medicine")
      .count()
      .sort($"count".desc)
    medicineCount.show()
    medicineCount
      .coalesce(1)
      .write
      .format("csv")
      .mode(SaveMode.Overwrite)
      .save("hdfs://localhost:8020/disease/medicineCount")

    val ageDiseaseCount = formatData
      .groupBy("age", "disease")
      .count()
      .sort($"count".desc)
    ageDiseaseCount.show()
    ageDiseaseCount
      .coalesce(1)
      .write
      .format("csv")
      .mode(SaveMode.Overwrite)
      .save("hdfs://localhost:8020/disease/ageDiseaseCount")

    val heightDiseaseCount = formatData
      .groupBy("height", "disease")
      .count()
      .sort($"count".desc)
    heightDiseaseCount.show()
    heightDiseaseCount
      .coalesce(1)
      .write
      .format("csv")
      .mode(SaveMode.Overwrite)
      .save("hdfs://localhost:8020/disease/heightDiseaseCount")

    val weightDiseaseCount = formatData
      .groupBy("weight", "disease")
      .count()
      .sort($"count".desc)
    weightDiseaseCount.show()
    weightDiseaseCount
      .coalesce(1)
      .write
      .format("csv")
      .mode(SaveMode.Overwrite)
      .save("hdfs://localhost:8020/disease/weightDiseaseCount")

    spark.stop()

  }
}
