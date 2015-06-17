package sparkExamples.invertedIndices

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by lucykur on 17/06/15.
 */
object invertedIndices {

  def main(arg: Array[String]) = {

    val inputPath = "hdfs:///user/lucykur/invertedIndices/sample.txt"
    val outputPath = "hdfs:///user/lucykur/invertedIndices/results"

    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local")
        .setAppName("inverted indices data analysis")
    )

    def getWordCountByLine(b: Iterable[(String, (Long, Int))]): Map[Long, Int] = {
      b.groupBy { case (word, (lineNum, count)) => lineNum}.mapValues(_.size)
    }

    sc.textFile(inputPath)
      .zipWithIndex()
      .flatMap { case (line, lineNum) => {line.split(" ").map(word => (word, (lineNum + 1, 1)))}}
      .groupBy { case (word, (_, _)) => word}
      .map { case (word, b) => (word, b.size, getWordCountByLine(b))}
      .saveAsTextFile(outputPath)


  }

}
