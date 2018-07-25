package cn.just

import java.io.File

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object NetworkWordCount {

  def main(args: Array[String]): Unit = {
    val path = new File(".").getCanonicalPath()
    //File workaround = new File(".");
    System.getProperties.put("hadoop.home.dir", path)
    new File("./bin").mkdirs()
    new File("./bin/winutils.exe").createNewFile()


    val conf =new SparkConf()
      .setMaster("local[2]")
      .setAppName("NetworkWordCount")

    val ssc=new StreamingContext(conf,Seconds(5))
    //使用nc工具：nc -lk 12345
    val lines=ssc.socketTextStream("hadoop-senior.shinelon.com",12345)

    val results=lines.flatMap(_.split(" ")).map(word=>(word,1)).reduceByKey(_+_)

    results.print()

    //启动Spark Streaming
    ssc.start()

    ssc.awaitTermination()
  }


}
