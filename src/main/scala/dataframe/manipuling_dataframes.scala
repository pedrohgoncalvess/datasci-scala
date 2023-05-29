package dataframe

object manipuling_dataframes extends App {

  import spark.initSpark.createSparkSession
  import org.apache.spark.sql.functions._

  val sparkS = createSparkSession
  val sc = sparkS.sparkContext

  val df = sparkS.range(0,10).select("id")
  .withColumn("uniform",rand(10L)).withColumn("normal",rand(10L))

  val halfToNaN = udf[Double, Double] (x => if (x > 0.5) Double.NaN else x) /*creating a dataframe with NaN values*/
  val oneToNaN = udf[Double, Double] (x => if (x > 1.0) Double.NaN else x)
  val dfNaN = df.withColumn("nanUniform",halfToNaN(df("uniform")))
  .withColumn("nanNormal",oneToNaN(df("normal"))).drop("uniform")
  .withColumnRenamed("nanUniform","uniform").drop("normal")
  .withColumnRenamed("nanNormal","normal")
  dfNaN.show()

  dfNaN.na.drop(minNonNulls=3).show() //dropping rows with NaN value
  dfNaN.na.drop("any",Array("uniform","normal")).show()

  dfNaN.na.fill(0.0).show() //replacing nan values with 0


  val uniformMean = dfNaN.filter("uniform <> 'NaN'")  //get mean values without nan
    .groupBy().agg(mean("uniform")).first()(0)

  dfNaN.na.fill(Map("uniform" -> uniformMean)).show(5) //replace nan values with uniform mean

  val dfCols = dfNaN.columns.drop(1)
  val dfMeans = dfNaN.na.drop().
    groupBy().agg(mean("uniform"),mean("normal")).first().toSeq
  val meansMap = (dfCols.zip(dfMeans)).toMap
  dfNaN.na.fill(meansMap).show(5)


  dfNaN.na.replace("uniform",Map(Double.NaN -> 0.0)).show() //replacing nan values with 0.0

  val dfDuplicates = df.unionAll(sparkS.createDataFrame(Seq((10,1,1),(11,1,1))).toDF()) //create dataframe with duplicate values
  dfDuplicates.show()
  dfDuplicates.dropDuplicates(dfCols).show()
}
