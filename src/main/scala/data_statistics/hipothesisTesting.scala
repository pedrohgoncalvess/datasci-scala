package data_statistics

import org.apache.spark.mllib.linalg.{Matrices, Matrix, Vector, Vectors}
import org.apache.spark.mllib.random.RandomRDDs.{normalRDD, uniformRDD}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.stat.test.ChiSqTestResult
import org.apache.spark.mllib.stat.{KernelDensity, Statistics}
import org.apache.spark.rdd.RDD
import spark.initSpark.createSparkSession


object Hipothesis extends App { //HIPOTHESIS TESTS

  val vec: Vector = Vectors.dense(
    0.3,0.2,0.1,0.1,0.1,0.05
  )
  val goodnessOfFitTestResult = Statistics.chiSqTest(vec)
  println(goodnessOfFitTestResult)
}

object Independence extends App { //INDEPENDENCE TESTS

  val mat: Matrix = Matrices.dense(3,2,
  Array(13.0,47.0,40.0,80.0,11.0,9.0)
  )

  val independenceTestResult = Statistics.chiSqTest(mat)
  //println(independenceTestResult)

  val sc = createSparkSession.sparkContext

  val obs: RDD[LabeledPoint] = sc.parallelize(Array(
    LabeledPoint(0,Vectors.dense(1.0,2.0)),
    LabeledPoint(0,Vectors.dense(0.5,1.5)),
    LabeledPoint(1,Vectors.dense(1.0,8.0)),
  ))

  val featureTestResults: Array[ChiSqTestResult] = Statistics.chiSqTest(obs)
  featureTestResults.foreach(println)
}

object EqualityDistribution extends App { //TEST FOR EQUALITY OF DISTRIBUTION

  val sc = createSparkSession.sparkContext

  val normalData: RDD[Double] = normalRDD(sc, size=100, numPartitions=1, seed=13L)
  val testResultNormal = Statistics.kolmogorovSmirnovTest(normalData,"norm",0,1)
  println(testResultNormal)

  val uniformData: RDD[Double] = uniformRDD(sc,size=100,numPartitions=1,seed=13L)
  val testResultUniform = Statistics.kolmogorovSmirnovTest(uniformData,"norm",0,1)
  println(testResultUniform)
}

object KernelDensityEstimation extends App {
  val sc = createSparkSession.sparkContext

  val normalData: RDD[Double] = normalRDD(sc,size = 1000, numPartitions = 1, seed = 17L)

  val kdNormal = new KernelDensity().setSample(normalData).setBandwidth(0.1)

  val normalDensities = kdNormal.estimate(Array(-1.5,-1,-0.5,0,0.5,1,1.5))
  normalDensities.foreach(println)


  val uniformData: RDD[Double] = normalRDD(sc, size = 1000, numPartitions = 1, seed = 17L)

  val kdUniform = new KernelDensity().setSample(uniformData).setBandwidth(0.1)

  val uniformDensities = kdUniform.estimate(Array(-1.5, -1, -0.5, 0, 0.5, 1, 1.5))
  uniformDensities.foreach(println)

}
