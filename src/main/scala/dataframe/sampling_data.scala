package dataframe

object sampling_data extends App {

  import spark.initSpark.createSparkSession

  val sparkS = createSparkSession
  val sc = sparkS.sparkContext

  val df = sparkS.createDataFrame(Seq((1,10),(1,20),(2,10),
    (2,20),(2,30),(3,20),(3,30))).toDF("key","value")

  val dfSampled = df.sample(fraction=0.3,seed=11L) /* random 30% of dataframe */
  dfSampled.show()

  val dfRandomSplit = df.randomSplit(weights=Array(0.3,0.7), seed=11L) /*creating a array of random dataframes*/
  dfRandomSplit(0).show()
  dfRandomSplit(1).show()

  val dfStrat = df.stat.sampleBy(col="key", /*stratified sampled dataframe*/
    fractions=Map(1 -> 0.7, 2 -> 0.7, 3 -> 0.7),
    seed=11L)
  dfStrat.show()

  import org.apache.spark.sql.functions.{rand,randn}

  val randomDf = sparkS.range(0,10)
  randomDf.select("id")
    .withColumn("uniform",rand(10L))
    .withColumn("normal",randn(10L)).show()
}
