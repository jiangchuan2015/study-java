package chuan.study.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object WordCount {
  def main(args: Array[String]) {
    // val inputFile = "D:\\big-data\\spark\\word-count.txt"
    val inputFile = "hdfs://big-data-01:9000/spark/input/word-count.txt";
    val conf = new SparkConf().setAppName("WordCount").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("Warn")

    val textFile = sc.textFile(inputFile)
    val wordCount = textFile.flatMap(line => line.replaceAll("[,.-]", " ").split(" "))
      .map(word => (word, 1))
      .reduceByKey((a, b) => a + b)
      .sortBy(x => (x._2, x._1), false)
      .take(10)

    wordCount.foreach(println)

    sc.stop()
  }
}
