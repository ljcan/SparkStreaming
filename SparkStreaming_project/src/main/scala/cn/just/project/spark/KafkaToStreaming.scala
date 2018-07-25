package cn.just.project.spark

import cn.just.project.spark.dao.{CourseClickCountDao, CourseSearchCountDao}
import cn.just.project.spark.domain.{CheckLog, CourseClickCount, CourseSearchCount}
import cn.just.project.spark.utils.DateUtils
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer

object KafkaToStreaming {

  def main(args: Array[String]): Unit = {

    val conf=new SparkConf()
      .setAppName("KafkaToStreaming")
      .setMaster("local[2]")
    //一分钟执行一次
    val ssc=new StreamingContext(conf,Seconds(60))

//    val version= scala.util.Properties.releaseVersion
//    println(version)

    val topciMap=Map("Streaming"->1)

    val kafkaStreaming=KafkaUtils.createStream(ssc,"hadoop-senior04.shinelon.com:2181","test",topciMap).map(_._2)

//    kafkaStreaming.count().print()

   val LogInfo= kafkaStreaming.map(lines=>{
      val infos=lines.split("\t")

      val ip=infos(0)
      val time=DateUtils.parseTime(infos(1))
      var courseId=0
      val url=infos(2)
        .split(" ")(1)
      if(url.startsWith("/class")){
            val courceHTML=url.split("/")(2)
            courseId=courceHTML.substring(0,courceHTML.lastIndexOf(".")).toInt
          }
          CheckLog(ip,time,courseId,infos(3).toInt,infos(4))
     }).filter(checkLog=>checkLog.courseId!=0)                     //过滤掉courseId为0的数据

    LogInfo.print()

    /**
      * 从今天到现在为止实战课程的访问量写入数据库
      */
    LogInfo.map(x=>{
      //将CheckLog格式的数据转为20180724_courseId
      (x.time.substring(0,8)+"_"+x.courseId,1)
    }).reduceByKey(_+_).foreachRDD(rdd=>{
      rdd.foreachPartition(partition=>{
        val list=new ListBuffer[CourseClickCount]
        partition.foreach(info=>{
          list.append(CourseClickCount(info._1,info._2))
        })
        CourseClickCountDao.save(list)      //保存到HBase数据库
      })
    })

    /**
      * 统计从搜索引擎引流过来的访问量，最后写入数据库
      */
    LogInfo.map(x=>{
      /**
        * https://www.sogou.com/web?query=
        *
        * =>https:/www.sogou.com/web?query=
        */
      val url=x.referes.replaceAll("//","/")
      val splits=url.split("/")
      var search=""
      if(splits.length>=2){
        search=splits(1)
      }
      (x.time.substring(0,8),x.courseId,search)
    }).filter(x=>x._3!="").map(info=>{
      val day_search_course=info._1+"_"+info._3+"_"+info._2
      (day_search_course,1)
    }).reduceByKey(_+_).foreachRDD(rdd=>{
      rdd.foreachPartition(partition=>{
        val list=new ListBuffer[CourseSearchCount]
        partition.foreach(info=>{
          list.append(CourseSearchCount(info._1,info._2))
        })
        CourseSearchCountDao.save(list)      //保存到HBase数据库
      })
    })

    ssc.start()

    ssc.awaitTermination()
  }


}
