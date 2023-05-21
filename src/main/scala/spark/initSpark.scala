package spark

import org.apache.spark.sql.SparkSession

object _initSparkSession{
  def createSparkSession: SparkSession = {
    val ss = SparkSession.builder()
      .appName("Creating Spark Session")
      .master("local")
      .getOrCreate()
    ss
  }
}
