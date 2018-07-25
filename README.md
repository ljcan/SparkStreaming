# Sparing Streaming实时日志分析

* 前言：使用scala和java混编完成，其中也涉及到python脚本来自动生成日志，linux crontab调度工具来定时执行脚本生成实时日志。生成的数据主要是模拟某学习网站学习视频课程的访问量（其中模拟的日志中URL以"/class"开头的表示实战课程，然后通过流水线Flume+Kafka+SparkStreaming进行实时日志的收集，HBase来存储数据）*

### 注意事项（使用的软件工具及环境配置）
1. hadoop-2.6.0-cdh5.7.0
2. hbase-1.2.0-cdh5.7.0
3. zookeeper-3.4.5-cdh5.7.0
4. spark-2.2.0-bin-2.6.0-cdh5.7.0
5. apache-flume-1.6.0-cdh5.7.0-bin
6. kafka_2.11-0.9.0.0
7. apache-maven-3.3.9 
8. jdk1.8.0_181
9. scala-2.11.8

## 需求分析

1. 统计该网站实战课程的访问量。
2. 统计该网站实战课程从不同搜索引擎引流过来的访问量，通过结果可为该网站的课程广告投资的方向做出更准确的决策。

## 数据清洗
 
1. 使用Spark Streaming剔除掉不符合要求的数据。
2. 过滤URL以"/class"开头的数据清洗出实战课程相关数据。

## 数据分析

使用Spark Streaming进行实时日志分析然后写入HBase数据库。

# Spark Streaming Web可视化展示

* 使用SpringBoot+Echarts开发可视化展示页面 *

## 可视化部分结果展示

### 需求一结果展示

![SpaarkStreaming01](https://github.com/ljcan/jqBlogs/blob/master/SparkSql/pictures/QQ%E6%88%AA%E5%9B%BE20180725185011.png)

### 需求二结果展示

![SpaarkStreaming02](https://github.com/ljcan/jqBlogs/blob/master/SparkSql/pictures/QQ%E6%88%AA%E5%9B%BE20180725185029.png)


## 为什么使用HBase来存储数据？

在数据量大的情况下存储数据，因为是实时存储数据，在向数据库中写入数据的时候，需要按天来累加访问量，如果使用其他的RDBMS，需要进行多步API操作，先按插入数据，然后按条件查询数据，最后更新数据，这样在一定会使应用程序变慢很多，也消耗系统资源。而如果使用HBase利用它的前缀查询，只需一步API就可以存储相应的数据。


