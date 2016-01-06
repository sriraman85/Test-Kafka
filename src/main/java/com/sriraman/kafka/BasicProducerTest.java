package com.sriraman.kafka;

import kafka.admin.AdminUtils;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.clients.producer.*;
import org.joda.time.DateTime;

import java.util.Properties;
import java.util.Random;

/**
 * Created by Sriraman S. on 12/17/2015.
 */
public class BasicProducerTest {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BasicProducerTest.class);
    public static void main(String[] args) {
        if (args.length != 3){
            System.out.println("Usage: BasicProducerTest <broker list> <zookeeper> <topic>");
            System.exit(-1);
        }

        log.debug("Using broker list:" + args[0] + ", zk conn:" + args[1]);

        // long events = Long.parseLong(args[0]);
        Random rnd = new Random();

        Properties props = new Properties();
        props.put("bootstrap.servers", args[0]);
//        props.put("zk.connect", args[1]);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        props.put("retry.backoff.ms","1000");
        props.put("message.send.max.retries","10");
        props.put("client.id", "KafkaProducer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

//        props.put("topic.metadata.refresh.interval.ms","0");


        String TOPIC = args[2];
        //Check if topic exists. If not create.
        ZkClient zkClient = new ZkClient(args[1], 10000, 10000, ZKStringSerializer$.MODULE$);
        ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(args[1],10000),false);
        if (!AdminUtils.topicExists(zkUtils,TOPIC)){
            AdminUtils.createTopic(zkUtils, TOPIC, 5, 1, new Properties());
        }

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);
        long counter = 0;
        while (true){

            try {
                String message = new DateTime().toString() + "||" + counter;
                log.info("Sending message #" + counter);
                ProducerRecord<String,String> keyedMessage = new ProducerRecord<String, String>(TOPIC,((Long)counter).toString(),message);
                producer.send(keyedMessage, new Callback() {
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        System.out.println("sent message");
                    }
                });
                counter++;
                Thread.sleep(900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
