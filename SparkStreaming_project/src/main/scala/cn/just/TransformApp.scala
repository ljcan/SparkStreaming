package cn.just

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * transform算子案例：黑名单的过滤
  */
object TransformApp {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf()
      .setAppName("TransformApp")
      .setMaster("local[2]")

    //batch size
    val ssc=new StreamingContext(conf,Seconds(5))

    //使用nc工具：nc -lk 12345
    val lines=ssc.socketTextStream("hadoop-senior.shinelon.com",12345)

    /**
      * 构建黑名单
      * 实际中存在数据库中
      */
    val blacks=List("jack","leo")
    //转换为RDD   jack=>(jack,true)
    val blacksRDD=ssc.sparkContext.parallelize(blacks).map(x=>(x,true))
    //2018722,shinelon=>shienlon,[2018722,shinelon]
    val checkLog=lines.map(x=>(x.split(",")(1),x)).transform(rdd=>{
      rdd.leftOuterJoin(blacksRDD).
        filter(x=>x._2._2.getOrElse(false)!=true)    //不是黑名单的成员留下
        .map(name=>name._2._1)
    })

    checkLog.print()

    ssc.start()

    ssc.awaitTermination()
  }

}
