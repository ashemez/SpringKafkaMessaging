package com.endpoint.SpringKafkaMessaging.message.broker;

import com.endpoint.SpringKafkaMessaging.message.MessageService;
import com.endpoint.SpringKafkaMessaging.websocket.MessageHandler;
import com.endpoint.SpringKafkaMessaging.websocket.WebSocketPool;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    MessageService messageService;

    @Autowired
    MessageHandler messageHandler;

    @KafkaListener(topics = "SEND_MESSAGE")
    public void messagesSendToUser(@Payload String message, @Headers MessageHeaders headers) {

        JSONObject jsonObject = new JSONObject(message);

        if (WebSocketPool.websockets.get(jsonObject.getString("sendTo")) != null) {

            String accessToken = jsonObject.getString("accessToken");
            Long sendTo = Long.parseLong(jsonObject.getString("sendTo"));
            String msg = jsonObject.getString("msg");

            messageService.sendMessage(accessToken, sendTo, msg);

        }
    }

}