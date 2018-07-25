package cn.just.spark.kafka.producer;

/**
 * 配置属性常量
 */
public class KafkaProperties {
    public static final String ZK="hadoop-senior04.shinelon.com:2181";      //Zookeeper地址
    public static final String TOPIC="topic02";                          //topic名称
    public static final String BROKER_LIST="hadoop-senior04.shinelon.com:9092";    //Broker列表
    public static final String GROUP_ID="test_group01";                 //消费者使用
}
