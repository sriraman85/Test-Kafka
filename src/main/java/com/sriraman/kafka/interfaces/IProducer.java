package com.sriraman.kafka.interfaces;

import java.util.Properties;

/**
 * Created by Sriraman S. on 01-01-2016.
 */
public interface IProducer {
    /**
     * Add the different properties
     * Currently the following properties are supported
     * metadata.broker.list,--broker list <IP1>:<port1>,<IP2><port2> eg: 127.0.0.1:9902,127.0.0.1:9093
     * zk.connect, -- zookeeper connection  string <IP1>:<port1>,<IP2><port2> eg: 127.0.0.1:2181,127.0.0.1:2182
     * serializer.class, -- class name to be used for serialization including the complete package
     * request.required.acks,--whether ack
     * retry.backoff.ms,
     * message.send.max.retries,
     * topic.metadata.refresh.interval.ms
     *
     * @param properties
     * @param schema
     * @param topic
     */
    void init(Properties properties, String schema, String topic);
    void produce(Properties message, Properties key, IAckCallback callback);
}
