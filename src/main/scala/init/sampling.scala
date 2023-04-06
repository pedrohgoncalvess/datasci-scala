package init

import org.apache.spark.mllib.linalg.{Matrices, Matrix, Vector, Vectors}
import init._initSparkSession.createSparkSession
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.distributed.IndexedRow
import org.apache.spark.rdd.RDD

object sampling extends App{
  val sc: SparkContext = createSparkSession.sparkContext

  val elements: RDD[Vector] = sc.parallelize(Array(
    Vectors.dense(4.0,7.0,13.0),
    Vectors.dense(-2.0,8.0,4.0),
    Vectors.dense(3.0,-11.0,19.0)
  ))

  val elementSeed7 = elements.sample(withReplacement=false,fraction=0.5,seed=7L).collect()
  elementSeed7.foreach(println)

  val elementSeed10 = elements.sample(withReplacement = false, fraction = 0.5, seed = 10L).collect()
  elementSeed10.foreach(println)

  val elementSeed64 = elements.sample(withReplacement = false, fraction = 0.5, seed = 64L).collect()
  elementSeed64.foreach(println)


  //SIMPLE RANDOM SPLIT

  val data = sc.parallelize(1 to 100000)
  val splits = data.randomSplit(Array(0.6,0.2,0.2), seed=13L) //SPLIT IN 3 ARRAYS WHERE FIRST ARRAY CONTAINS 0.6 (60%) OF VALUES, SECOND 0.2 (20%) AND THIRTH SAME

  val training = splits(0)
  val test = splits(1)
  val validation = splits(2)

  print(training,test,validation)


  //STRATIFIED SAMPLING

  val indexedRows: RDD[IndexedRow] = sc.parallelize(Array(
    IndexedRow(0,Vectors.dense(1.0,2.0)),
    IndexedRow(1,Vectors.dense(4.0,5.0)),
    IndexedRow(1,Vectors.dense(7.0,8.0))
  ))

  val fractions: Map[Long,Double] = Map(0L -> 1.0, 1L -> 0.5)

  val approxSample = indexedRows.map{
    case IndexedRow(index, vec) => (index,vec)
  }.sampleByKey(withReplacement = false,fractions,9L)

  approxSample.collect().foreach(println)

}