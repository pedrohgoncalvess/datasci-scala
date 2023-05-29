package dataframe

object data_normalization extends App{

  import spark.initSpark.createSparkSession
  import org.apache.spark.sql.functions._
  import org.apache.spark.ml.feature.{VectorAssembler,Normalizer,StandardScaler,MinMaxScaler}

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
  dfVec.select("id","features").show()

  val scaler1 = new Normalizer().setInputCol("features").setOutputCol("scaledFeat").setP(1.0)

  scaler1.transform(dfVec.select("id","features")).show()

  val scaler2 = new StandardScaler().setInputCol("features").setOutputCol("scaledFeat").setWithStd(true).setWithMean(true)
  val scaler2Model = scaler2.fit(dfVec.select("id","features"))
  scaler2Model.transform(dfVec.select("id","features")).show(5)

  val scaler3 = new MinMaxScaler().setInputCol("features").setOutputCol("scaledFeat").setMin(-1.0).setMax(1.0)
  val scaler3Model = scaler3.fit(dfVec.select("id","features"))
  scaler3Model.transform(dfVec.select("id","features")).show(5)
}
