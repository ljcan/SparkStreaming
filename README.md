# Sparing Streaming实时日志分析

* 前言：使用scala和java混编完成，其中也涉及到python脚本来自动生成日志，linux crontab调度工具来定时执行脚本生成实时日志。生成的数据主要是模拟某学习网站学习视频课程的访问量（其中模拟的日志中URL以"/class"开头的表示实战课程，然后通过流水线Flume+Kafka+SparkStreaming进行实时日志的收集）*

## 需求分析

1. 统计该网站实战课程的访问量。
2. 统计该网站实战课程从不同搜索引擎引流过来的访问量，通过结果可为该网站的课程广告投资的方向做出更准确的决策。

## 数据清洗
 
1.使用Spark Streaming剔除掉不符合要求的数据。
2. 过滤URL以"/class"开头的数据清洗出实战课程相关数据。

## 数据分析

使用Spark Streaming进行实时日志分析然后写入HBase数据库。

# Spark Streaming Web可视化展示

* 使用SpringBoot+Echarts开发可视化展示页面 *

## 可视化部分结果展示

### 需求一结果展示



### 需求二结果展示


