package com.sriraman.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sriraman S. on 12/17/2015.
 */
public class HighLevelConsumerTest {
    private static final Logger log = Logger.getLogger(HighLevelConsumerTest.class);
    private KafkaConsumer<byte[],byte[]> consumer;
//    private ConsumerIterator<byte[],byte[]> it;
    private String topic;
    private int batchUpperLimit;
    private int timeUpperLimit = 250;
    private int consumerTimeout;
    private boolean kafkaAutoCommitEnabled;
    private Properties kafkaProps;

    public HighLevelConsumerTest(String a_zookeeper, String bootstrapServers, String a_groupId, String a_topic){
        /*consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));*/
        consumer = new KafkaConsumer<byte[], byte[]>(createConsumerConfig(a_zookeeper,a_groupId, bootstrapServers));
        consumer.subscribe(Arrays.asList(a_topic));
        this.topic = a_topic;
    }


    public void shutdown() {
        if (consumer != null) consumer.close();
    }

    private static Properties createConsumerConfig(String a_zookeeper, String a_groupId, String bootstrapServers) {
        Properties props = new Properties();
//        props.put("zookeeper.connect", a_zookeeper);
        props.put("bootstrap.servers",bootstrapServers);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
//        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.commit.enable","false");
        props.put("consumer.timeout.ms",""+TimeUnit.MINUTES.toMillis(1));
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    public void run() throws InterruptedException {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        while (true){
            ConsumerRecords<byte[],byte[]> records = consumer.poll(10000);
            /*if (records.isEmpty()){
                Thread.sleep(TimeUnit.MINUTES.toMillis(2));
                continue;
            }*/
            for (ConsumerRecord<byte[],byte[]> record: records){
                log.info("offset " + record.offset() + " key " +record.key() + " value " + record.value() + " partition " + record.partition());
            }
        }
    }

    /**
     * Check if there are messages waiting in Kafka,
     * waiting until timeout (10ms by default) for messages to arrive.
     * and catching the timeout exception to return a boolean
     */
    /*boolean hasNext() throws InterruptedException {
       *//* try {
            it.hasNext();
            return true;
        } catch (ConsumerTimeoutException e) {
            Thread.sleep(TimeUnit.MINUTES.toMillis(1));
            return hasNext();
        }*//*
    }*/

    /*private ConsumerRecords<byte[],byte[]> getRecords(){
        ConsumerRecords<byte[],byte[]> records = consumer.poll(100);
        int counter = 0;
        do {

        }while (counter)
        if (records.isEmpty()){

        }
    }*/

    public static void main(String[] args) {
        if (args == null || args.length != 3){
            System.out.println("Missing Arguments --> <zookeeper-list> <groupId> <topic>");
        }
        HighLevelConsumerTest consumerTest = new HighLevelConsumerTest(args[0], args[1], args[2],args[3]);

        try{
            consumerTest.run();
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
//        consumerTest.shutdown();
    }
}
