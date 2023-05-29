package dataframe

object displaying_data extends App {

  import spark.initSpark.createSparkSession
  import org.apache.spark.sql.functions._

  val sparkS = createSparkSession

  import sparkS.implicits._

  case class Record(desc:String,Value1:Int,value2:Double)

  val recDF = sparkS.sparkContext.parallelize(Seq( /* creating data*/
    ("first", 1, 3.7),
    ("second", -2, 2.1),
    ("third", 6, 0.7)
  )).toDF("desc", "Value1", "value2").as[Record]

  val recStats = recDF.describe()
  recStats.show()

  println(recStats.filter("summary = 'stddev'").first()) //array of values
  println(recStats.filter("summary = 'stddev'").first().toSeq.toArray.mkString(" ")) //string separated for blank value
  println(recStats.select("value1").map(s => s(0).toString.toDouble).collect().mkString(" "))

  println(recDF.groupBy().agg(Map("value1" -> "min", "value1" -> "max")))
  println(recDF.groupBy().agg(Map("value1" -> "min", "value2" -> "min")))

  val recStatsGroup = recDF.groupBy().agg(min("value1"),min("value2"))
  recStatsGroup.columns.foreach(println) //print columns
  recStatsGroup.first().toSeq.toArray.map(_.toString.toDouble).foreach(println) //print values

  val recDFStat = recDF.stat
  println(recDFStat.corr("value1","value2"))
  println(recDFStat.cov("value1","value2"))
  recDFStat.freqItems(Seq("value1"),0.3).show()

}
