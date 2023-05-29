package dataframe

object transformers_estimators extends App{

  import org.apache.spark.ml.feature.{Tokenizer, RegexTokenizer}
  import spark.initSpark.createSparkSession
  import org.apache.spark.sql.functions._
  import org.apache.spark.ml.linalg.{Vectors, Vector}
  import org.apache.spark.ml.classification.LogisticRegression
  import org.apache.spark.ml.param.ParamMap
  import org.apache.spark.ml.feature.VectorAssembler

  val sparkS = createSparkSession
  val sc = sparkS.sparkContext

  val sentenceDataFrame = sparkS.createDataFrame(
    Seq((0,"Hi I heard about Spark"),
    (1,"I wish Java could use case classes"),
    (2,"Logistic, regression, models,are,neat")
  )).toDF("label","sentence")

  val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
  val tokenized = tokenizer.transform(sentenceDataFrame)

  tokenized.show()

  val training = sparkS.createDataFrame( /*creating dataframe to training*/
    Seq(
      (1.0, Vectors.dense(0.0,1.1,0.1)),
      (0.0, Vectors.dense(2.0,1.0,-1.0)),
      (0.0, Vectors.dense(2.0,1.3,1.0)),
      (1.0, Vectors.dense(0.0,1.2,-0.5))
    )
  ).toDF("label","features")

  val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.01)
  val model1 = lr.fit(training)
  model1.transform(training).show()

  val paramMap = ParamMap(lr.maxIter -> 20, lr.regParam -> 0.01)
  val model2 = lr.fit(training, paramMap)
  model2.transform(training).show()

  val dfRandom = sparkS.range(0,10).select("id")
    .withColumn("uniform",rand(10L))
    .withColumn("normal1",randn(10L))
    .withColumn("normal2",randn(11L))

  val assembler = new VectorAssembler().
    setInputCols(Array("uniform","normal1","normal2"))
    .setOutputCol("features")

  val dfVec = assembler.transform(dfRandom)
  dfVec.show()
}
