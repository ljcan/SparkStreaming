package cn.just

import java.io.File

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StatefulWordCount {

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

    //使用 stateful算子必须设置checkPoint
    //在生产环境中建议将checkPoint设置为hdfs文件系统中的一个文件
    ssc.checkpoint(".")

    //使用nc工具：nc -lk 12345
    val lines=ssc.socketTextStream("hadoop-senior.shinelon.com",12345)

    val results=lines.flatMap(_.split(" ")).map(word=>(word,1)).reduceByKey(_+_)

     val state=results.updateStateByKey[Int](updateFunc)

    state.print()

    //启动Spark Streaming
    ssc.start()

    ssc.awaitTermination()
  }

 val updateFunc = (values: Seq[Int], state: Option[Int]) => {
  val currentCount = values.sum
  val previousCount = state.getOrElse(0)
  Some(currentCount + previousCount)
 }


}
