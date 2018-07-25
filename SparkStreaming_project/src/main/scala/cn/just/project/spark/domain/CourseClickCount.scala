package cn.just.project.spark.domain

/**
  * Hbase数据库实体类
  * 课程点击实体类
  * day_course:rowkey
  * clickcount:column
  */
case class CourseClickCount(day_course:String,clickcount:Long)
