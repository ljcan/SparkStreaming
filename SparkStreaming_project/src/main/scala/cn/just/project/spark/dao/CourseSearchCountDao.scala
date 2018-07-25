package cn.just.project.spark.dao

import cn.just.project.spark.domain.{CourseClickCount, CourseSearchCount}
import cn.just.spark.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

/**
  * 数据库访问层
  * 从搜索引擎引流过来的访问量
  * create 'course_search_clickcount','info'
  *
  */
object CourseSearchCountDao {

  val tableName="course_search_clickcount"    //表名
  val cf="info"                         //列簇
  val qualifer="clickcount"               //列名

  /**
    * 向数据库中保存数据
    * @param courseSearchList
    */
  def save(courseSearchList:ListBuffer[CourseSearchCount]): Unit ={
      val table=HBaseUtils.getInstance().getTable(tableName)
      for(ele <- courseSearchList){
        table.incrementColumnValue(
          Bytes.toBytes(ele.day_dearch_course),      //rowkey
          Bytes.toBytes(cf),                  //列簇
          Bytes.toBytes(qualifer),            //列名
          ele.clickcount                      //值
        )
      }
  }

  /**
    * 从数据中读取信息
    * @param day_search_course
    * @return
    */
  def get(day_search_course:String):Long={
    val table=HBaseUtils.getInstance().getTable(tableName)

    val get=new Get(Bytes.toBytes(day_search_course))      //rowkey

    val value=table.get(get).getValue(Bytes.toBytes(cf),Bytes.toBytes(qualifer))

    if(value==null){
      0l
    }else{
      Bytes.toLong(value)
    }
  }


  def main(args: Array[String]): Unit = {
//    val list=new ListBuffer[CourseSearchCount]
//    list.append(CourseSearchCount("20180724_souhu.com_45",85))
//    list.append(CourseSearchCount("20180724_baidu.com_124",45))
//    save(list)

    println(get("20180724_souhu.com_45"))


  }

}
