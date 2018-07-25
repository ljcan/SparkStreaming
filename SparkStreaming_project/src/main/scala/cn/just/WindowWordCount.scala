package cn.just

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * window窗口函数的使用
  */
object WindowWordCount {

  def main(args: Array[String]): Unit = {
    val conf=new SparkConf()
          .setAppName("WindowWordCount")
          .setMaster("local[2]")

    //batch size
    val ssc=new StreamingContext(conf,Seconds(5))

    //使用nc工具：nc -lk 12345
    val lines=ssc.socketTextStream("hadoop-senior.shinelon.com",12345)

    // window length 第一个参数，窗口的长度
    // sliding interval ，第二个参数，窗口的间隔
    //并且batch size要和这两个参数成倍数关系
    //每个十秒统计前三十秒的词频数
    val results=lines.flatMap(_.split(" ")).map((_,1)).reduceByKeyAndWindow((a:Int,b:Int) => (a + b),
      Seconds(30), Seconds(10))

    results.print()

    ssc.start()

    ssc.awaitTermination()



  }

}
