package init

import org.apache.spark.mllib.linalg.{Vectors,Matrix,Matrices}
import org.apache.spark.mllib.regression.LabeledPoint

object newApp extends App{


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

}
