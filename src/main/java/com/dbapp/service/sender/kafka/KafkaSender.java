package com.dbapp.service.sender.kafka;

import com.dbapp.bean.EffectiveConfig;
import com.dbapp.service.DisplayService;
import com.dbapp.service.sender.AbstractSender;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KafkaSender extends AbstractSender {

    private KafkaProducer<byte[], byte[]> producer;
    private Charset logEncoding;
    private String topic;

    @Override
    public String getName() {
        return "KafkaSender";
    }

    @Override
    public void init(EffectiveConfig config) throws Exception {
        Map<String, Object> prop = new HashMap<>();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getServer());
        prop.put(ProducerConfig.CLIENT_ID_CONFIG, "LogSender-KafkaSender");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        this.producer = new KafkaProducer<>(prop);
        this.logEncoding = config.getLogEncoding();
        this.topic = config.getTopic();
    }

    @Override
    public void send(List<byte[]> msg) {
        try {
            for (byte[] m : msg) {
                byte[] key = String.valueOf(System.currentTimeMillis()).getBytes(logEncoding);
                ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, key, m);
                this.producer.send(record);
            }
        } catch (Exception e) {
            DisplayService.directPrint("send kafka failed.", e);
        }
    }

    @Override
    public void stop() {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }
}
