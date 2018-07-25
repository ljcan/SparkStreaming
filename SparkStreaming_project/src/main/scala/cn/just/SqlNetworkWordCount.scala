package cn.just

import java.io.File

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}

/**
  * Spark Streaming整合Spark SQL
  */
object SqlNetworkWordCount {

  def main(args: Array[String]) {

    val conf =new SparkConf()
      .setMaster("local[2]")
      .setAppName("SqlNetworkWordCount")

    val ssc=new StreamingContext(conf,Seconds(5))

    val lines=ssc.socketTextStream("hadoop-senior.shinelon.com",12345)
    val words = lines.flatMap(_.split(" "))
    //RDD=>DataFrame
    words.foreachRDD { (rdd: RDD[String], time: Time) =>
      // Get the singleton instance of SparkSession
      val spark = SparkSessionSingleton.getInstance(rdd.sparkContext.getConf)

      import spark.implicits._
      // Convert RDD[String] to RDD[case class] to DataFrame
      val wordsDataFrame = rdd.map(w => Record(w)).toDF()

      // Creates a temporary view using the DataFrame
      //registe temp table
      wordsDataFrame.createOrReplaceTempView("words")

      // Do word count on table using SQL and print it
      val wordCountsDataFrame = spark.sql("select word, count(*) as total from words group by word")
      println(s"========= $time =========")
      wordCountsDataFrame.show()
    }

    ssc.start()
    ssc.awaitTermination()
  }

  /** Case class for converting RDD to DataFrame */
  case class Record(word: String)


  /** Lazily instantiated singleton instance of SparkSession */
  object SparkSessionSingleton {
    @transient  private var instance: SparkSession = _
    def getInstance(sparkConf: SparkConf): SparkSession = {
      if (instance == null) {
        instance = SparkSession
          .builder
          .config(sparkConf)
          .getOrCreate()
      }
      instance
    }
  }

}
