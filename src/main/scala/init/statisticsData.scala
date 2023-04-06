package init

import org.apache.spark.mllib.linalg.{Matrix, Vector, Vectors}
import org.apache.spark.rdd.RDD
import init._initSparkSession.createSparkSession
import org.apache.spark.SparkContext
import org.apache.spark.mllib.stat.{Statistics,MultivariateStatisticalSummary}
import org.apache.spark.mllib.random.RandomRDDs._


object statisticsData extends App{

  val sc: SparkContext = createSparkSession.sparkContext

  val observations: RDD[Vector] = sc.parallelize(Array( //BASIC DATASET
    Vectors.dense(1.5, 2.3),
    Vectors.dense(4.1, 5.7),
    Vectors.dense(7.2, 8.8)
  ))

  val summary: MultivariateStatisticalSummary = Statistics.colStats(observations) //STATISTICS ABOUT DATASET
  println("=============================SUMMARY=============================")

  println(summary.mean)
  println(summary.variance)
  println(summary.numNonzeros)
  println(summary.normL1)
  println(summary.normL2)

  //PEARSON CORRELATION BETWEEN TWO SERIES

  val x: RDD[Double] = sc.parallelize(Array(2.0,9.0,-7.0))
  val y: RDD[Double] = sc.parallelize(Array(3.0,10.0,-6.0))
  val correlation: Double = Statistics.corr(x,y,"pearson")
  println(correlation)

  //PEARSON CORRELATION AMONG SERIES
  val data: RDD[Vector] = sc.parallelize(Array( //CORRELATION IS 1 BECAUSE THE DIFFERENCE BETWEEN VALUES IS ONE
    Vectors.dense(1, 2),
    Vectors.dense(4, 5),
    Vectors.dense(7, 8)
  ))

  val correlMatrix: Matrix = Statistics.corr(data,"pearson")
  println(correlMatrix)


  //PEARSON X SPEARMAN CORRELATION
  val ranks: RDD[Vector] = sc.parallelize(Array( //CORRELATION IS 1 BECAUSE THE DIFFERENCE BETWEEN VALUES IS ONE
    Vectors.dense(1.0, 2.0,3.0),
    Vectors.dense(4.0, 5.0,6.0),
    Vectors.dense(7.0, 8.0,9.0)
  ))
  val corrPearsonMatrix: Matrix = Statistics.corr(ranks, "pearson")
  val corrSpearmanMatrix:Matrix = Statistics.corr(ranks,"spearman")

  println(corrPearsonMatrix)
  println(corrSpearmanMatrix)


  //RANDOM DATA

  val million = poissonRDD(sc,mean=1,size = 1000000L,numPartitions = 10)
  println(million.mean)
  println(million.variance)


  //SIMPLE VECTOR EXAMPLE
  val dataRandomVector = normalVectorRDD(sc,numRows=10000L, numCols = 3, numPartitions=10)
  val statsRandomVector: MultivariateStatisticalSummary = Statistics.colStats(dataRandomVector)
  println(statsRandomVector.mean)
  println(statsRandomVector.variance)
}
