package cn.just

import java.io.File
import java.sql.{Driver, DriverManager}

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 统计单词词频，将结果写入mysql数据库
  */
object ForeachRDDWordCount {

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

    results.foreachRDD(rdd=>{
      rdd.foreachPartition(partitions=>{
        val connection=createConnection()
        partitions.foreach(records=>{
          val select="select word from wordcount where word='"+records._1+"'"
          val wordIsExist=connection.createStatement().executeQuery(select)
          //如果数据库中已经存在该数据，更新count
          if(wordIsExist.next()){
            val updateCount="update wordcount set count=("+records._2+"+count) where word='"+records._1+"'"
            connection.createStatement().executeUpdate(updateCount)
          }else{  //如果不存在直接插入统计结果
            val insertWordCount="insert into wordcount(word,count) values('"+records._1+"',"+records._2+")"
            connection.createStatement().execute(insertWordCount)
          }
        })
        connection.close()
      })
    })


    //启动Spark Streaming
    ssc.start()

    ssc.awaitTermination()
  }

  /**
    * create table wordcount(
        word varchar(50) not null,
        count int(20) not null
      )charset utf8;
    * @return
    */
  def createConnection() ={
    Class.forName("com.mysql.jdbc.Driver")
    DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sparkstreaming","root","123456")
  }




}
