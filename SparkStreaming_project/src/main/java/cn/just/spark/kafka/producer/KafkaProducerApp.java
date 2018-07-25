package cn.just.spark.kafka.producer;


import cn.just.spark.kafka.consumer.KafkaConsumer;

public class KafkaProducerApp {
    //快捷键psvm
    public static void main(String[] args) {
        new KafkaProducer(KafkaProperties.TOPIC).start();

        new KafkaConsumer(KafkaProperties.TOPIC).start();
    }
}
