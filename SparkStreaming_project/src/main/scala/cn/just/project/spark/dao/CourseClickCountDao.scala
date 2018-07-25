package cn.just.project.spark.dao

import cn.just.project.spark.domain.CourseClickCount
import cn.just.spark.utils.HBaseUtils
import org.apache.hadoop.hbase.client.{Get, HTable}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

/**
  * 访问数据库DAO层
  * 今天到现在为止的实战课程的访问量
  */
object CourseClickCountDao {

  val tableName="course_clickcount"    //表名
  val cf="info"                         //列簇
  val qualifer="clickcount"               //列名

  /**
    * 向数据库中保存数据
    * @param courseList
    */
  def save(courseList:ListBuffer[CourseClickCount]): Unit ={
      val table=HBaseUtils.getInstance().getTable(tableName)
      for(ele <- courseList){
        table.incrementColumnValue(
          Bytes.toBytes(ele.day_course),      //rowkey
          Bytes.toBytes(cf),                  //列簇
          Bytes.toBytes(qualifer),            //列名
          ele.clickcount                      //值
        )
      }
  }

  /**
    * 从数据中读取信息
    * @param day_course
    * @return
    */
  def get(day_course:String):Long={
    val table=HBaseUtils.getInstance().getTable(tableName)

    val get=new Get(Bytes.toBytes(day_course))      //rowkey

    val value=table.get(get).getValue(Bytes.toBytes(cf),Bytes.toBytes(qualifer))

    if(value==null){
      0l
    }else{
      Bytes.toLong(value)
    }
  }


  def main(args: Array[String]): Unit = {
//    val list=new ListBuffer[CourseClickCount]
//    list.append(CourseClickCount("20180724_45",85))
//    list.append(CourseClickCount("20180724_124",45))
//    list.append(CourseClickCount("20180724_189",12))
//
//    save(list)

    println(get("20180724_125"))


  }

}
