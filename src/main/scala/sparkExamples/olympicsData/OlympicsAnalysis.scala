package sparkExamples.olympicsData

import org.apache.spark.SparkContext.rddToPairRDDFunctions
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

case class Record(name: String,
                  country: String,
                  year: Int,
                  category: String,
                  gold: Int,
                  silver: Int,
                  bronze: Int,
                  total: Int)

object OlympicsAnalysis {
  def main(arg: Array[String]) = {

    val inputPath = arg(0)
    val outputPath = "hdfs:///user/lucykur/results"
    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local")
        .setAppName("olympics data analysis")
    )
    val file = sc.textFile(inputPath)


    file.map(parse)
      .map(olympicRecord => ((olympicRecord.name, olympicRecord.category), 1))
      .reduceByKey(_+_)
      .filter { case ((name, sport), total) => total > 1}
      .saveAsTextFile(outputPath)

  }



  def parse(input: String): Record = {
    val split = input.split(",")
    Record(split(0),
      split(1),
      split(2).toInt,
      split(3),
      split(4).toInt,
      split(5).toInt,
      split(6).toInt,
      split(7).toInt)
  }
}
