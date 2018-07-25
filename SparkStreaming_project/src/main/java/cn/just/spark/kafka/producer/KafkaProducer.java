package cn.just.spark.kafka.producer;



import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import scala.collection.Seq;

import java.util.Properties;

/**
 * Kafka java API:Producer
 */
public class KafkaProducer extends Thread{

    public String topic;

    public Producer<Integer,String> producer;

    public KafkaProducer(String topic){
        this.topic=topic;

        Properties properties=new Properties();

        properties.put("metadata.broker.list",KafkaProperties.BROKER_LIST);
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        //设置生产者与消费者的生产握手机制：0代表不需要Broker回复消息
        //1表示等到Broker回复消息之后继续生产
        //-1表示需要所有的Broker都回复消息之后才继续，这种更严格，数据不会丢失，持久性更好
        properties.put("request.required.acks","1");

        ProducerConfig config=new ProducerConfig(properties);

        producer=new Producer<Integer, String>(config);
    }

    @Override
    public void run() {
        int messageId=1;
        while(true){
            String message="kafkaProducer"+messageId;
            producer.send(new KeyedMessage<Integer, String>(topic,message));
            System.out.println(message);
            messageId++;

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
