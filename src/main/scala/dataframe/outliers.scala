package dataframe

object outliers extends App {

  import spark.initSpark.createSparkSession
  import org.apache.spark.sql.functions._
  import org.apache.spark.ml.feature.{VectorAssembler,Normalizer,StandardScaler,MinMaxScaler}
  import org.apache.spark.ml.linalg.Vectors

  val sparkS = createSparkSession
  val sc = sparkS.sparkContext

  val dfRandom = sparkS.range(0, 10).select("id")
    .withColumn("uniform", rand(10L))
    .withColumn("normal1", randn(10L))
    .withColumn("normal2", randn(11L))

  val assembler = new VectorAssembler().
    setInputCols(Array("uniform", "normal1", "normal2"))
    .setOutputCol("features")

  val dfVec = assembler.transform(dfRandom)
  val dfOutlier = dfVec.select("id","features")
    .unionAll(sparkS.createDataFrame(Seq((10,Vectors.dense(3,3,3)))))

  dfOutlier.sort(dfOutlier("id").desc).show(5)

  val scaler = new StandardScaler().setInputCol("features").setOutputCol("scaledFeat").setWithStd(true).setWithMean(true)
  val scalerModel = scaler.fit(dfOutlier.select("id", "features"))
  val dfScaled = scalerModel.transform(dfOutlier).select("id","scaledFeat")
  dfScaled.sort(dfOutlier("id").desc).show(3)


  import org.apache.spark.mllib.stat.Statistics

  val rddVec = dfScaled.select("scaledFeat").rdd.map(_(0)
    .asInstanceOf[org.apache.spark.mllib.linalg.Vector])

  val colCov = Statistics.corr(rddVec)

}
