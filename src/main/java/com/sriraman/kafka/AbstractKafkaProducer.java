package com.sriraman.kafka;

import com.sriraman.kafka.interfaces.IAckCallback;
import com.sriraman.kafka.interfaces.IProducer;

import java.util.Properties;

/**
 * Created by Sriraman S. on 01-01-2016.
 */
public class AbstractKafkaProducer implements IProducer{


    public void init(Properties properties, String schema, String topic) {

    }

    public void produce(Properties message, Properties key, IAckCallback callback) {

    }
}
