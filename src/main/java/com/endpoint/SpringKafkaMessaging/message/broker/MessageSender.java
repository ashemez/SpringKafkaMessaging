package com.endpoint.SpringKafkaMessaging.message.broker;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message){

        kafkaTemplate.send(topic, message);
    }
}
