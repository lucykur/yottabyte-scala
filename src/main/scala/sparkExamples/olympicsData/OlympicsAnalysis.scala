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
    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local")
        .setAppName("olympics data analysis")
    )
    val file = sc.textFile(inputPath)


    val ((country, year), medals) = file.map(parse)
                            .filter(olympicRecord => olympicRecord.country.equalsIgnoreCase("United States"))
                            .map(usOlympicRecord => ((usOlympicRecord.country, usOlympicRecord.year), usOlympicRecord.total))
                            .reduceByKey(_+_)
                            .sortBy { case (_ , total) => -total}
                            .first()


    print(s"${country} in ${year} : ${medals}");
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
