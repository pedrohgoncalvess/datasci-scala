package init

import org.apache.spark.mllib.linalg.{Matrices, Matrix, Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.distributed.{CoordinateMatrix, MatrixEntry, RowMatrix}
import init._initSparkSession.createSparkSession
import org.apache.spark.SparkContext

object simpleDataStructures extends App{


  println("=================VECTORS=================")
  //VECTORS DENSE AND SPARSE EXAMPLES
  val vetorDense = Vectors.dense(55.0,0.0, 15.0,14.0,16.0)
  println(vetorDense)

  //SIZE = SIZE OF SPARSE ARRAY, FIRST ARRAY IS INDICES WHERE 0 NUMBERS ARE LOCATED AND SECOND ARRAY IS RAMAINING VALUES
  val vetorSparse = Vectors.sparse(6,Array(0,4,2,1),Array(44.0,55.0,6,5)) //CANNOT HAVE MORE NON-ZERO VALUES THAN ZERO VALUES
  println(vetorSparse.toDense) //CONVERT SPARSE VECTOR TO NORMAL ARRAY

  val vetorSparseSeq = Vectors.sparse(3,Seq((0,44.0),(2,55.0))) //MORE EXAMPLES BUT WITH SEQ
  println(vetorSparseSeq)



  //LABELED POINT EXAMPLES
  println("=================LABELED=================")

  val labeledDense = LabeledPoint(1.0, Vectors.dense(44.0,0.0,55.0))
  println(labeledDense)

  val labeledSparse = LabeledPoint(0.0, Vectors.sparse(3,Array(0,2),Array(44.0,55.0)))
  println(labeledSparse)


  //MATRICES
  println("=================MATRICES=================")

  val matricesDense = Matrices.dense(3,2,(Array(1,5,6,9,8,3)))
  println(matricesDense)

  val matricesSparse = Matrices.sparse(5,4,
    Array(0,0,1,2,2),
    Array(1,3),
    Array(34,55)
  )
  println(matricesSparse)



  //DISTRIBUTED MATRICES
  println("=================DISTRIBUTED MATRICES=================")

  val sc: SparkContext = createSparkSession.sparkContext

  val rows: RDD[Vector] = sc.parallelize(Array(
    Vectors.dense(1, 2),
    Vectors.dense(4, 5),
    Vectors.dense(7, 8)
  ))

  val mat: RowMatrix = new RowMatrix(rows)

  val m = mat.numRows()
  val n = mat.numCols()
  println(mat)
  println(m)
  println(n)


  println("CoordinateMatrix")

  val entries: RDD[MatrixEntry] = sc.parallelize(Array(
    MatrixEntry(0,0,9.0),
    MatrixEntry(1,1,8.0),
    MatrixEntry(2,1,6.0)
  ))

  val coordMat: CoordinateMatrix = new CoordinateMatrix(entries)

  val coordM = coordMat.numRows()
  val coordN = coordMat.numCols()
  println(coordMat)
  println(coordM)
  println(coordN)
}
