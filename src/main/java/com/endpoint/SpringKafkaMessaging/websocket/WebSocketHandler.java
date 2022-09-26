package com.endpoint.SpringKafkaMessaging.websocket;

import com.endpoint.SpringKafkaMessaging.cache.respository.CacheRepository;
import com.endpoint.SpringKafkaMessaging.message.broker.MessageReceiver;
import com.endpoint.SpringKafkaMessaging.message.broker.MessageSender;
import com.endpoint.SpringKafkaMessaging.persistent.model.User;
import com.endpoint.SpringKafkaMessaging.persistent.repository.UserRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    CacheRepository cacheRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageHandler messageHandler;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String parameters[] = session.getUri().getQuery().split("=");

        LOG.info("ws session url  " + session.getUri().getPath() + " ? " + session.getUri().getQuery());

        if(parameters.length == 2 && parameters[0].equals("accessToken")) {
            String accessToken = parameters[1];

            Long senderUserId = 0L;
            String senderId = cacheRepository.getUserIdByAccessToken(accessToken);

            if(senderId == null) {
                User sender = userRepository.findByToken(accessToken);
                if(sender != null) {
                    senderUserId = sender.getUserId();
                }
            } else {
                senderUserId = Long.valueOf(senderId);
            }

            LOG.info("Websocket connected, userId: " + senderUserId + " -- " + senderId);

            if (senderUserId == 0L) {
                return;
            }

            messageHandler.removeFromSessionToPool(senderUserId, session);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String parameters[] = session.getUri().getQuery().split("=");

        LOG.info("Websocket connection requested.");
        if(parameters.length == 2 && parameters[0].equals("accessToken")) {
            String accessToken = parameters[1];

            Long senderUserId = 0L;
            String senderId = cacheRepository.getUserIdByAccessToken(accessToken);

            if(senderId == null) {
                User sender = userRepository.findByToken(accessToken);
                if(sender != null) {
                    senderUserId = sender.getUserId();
                }
            } else {
                senderUserId = Long.valueOf(senderId);
            }

            LOG.info("Websocket connected, userId: " + senderUserId);

            if (senderUserId == 0L) {
                LOG.info("Websocket connection: user not found for given accessToken");
                return;
            }

            messageHandler.addSessionToPool(senderUserId, session);
        }
        else {
            LOG.info("Websocket connection: accessToken not provided");
            session.close();
        }

    }

    @Autowired
    private MessageSender sender;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {

        JSONObject jsonObject = new JSONObject(textMessage.getPayload());
        LOG.info(textMessage.getPayload());
        String topic = jsonObject.getString("topic");
        JSONObject message = jsonObject.getJSONObject("message");

        // only SEND_MESSAGE topic is available
        if(topic == null && !topic.equals("SEND_MESSAGE")) {
            return;
        }

        sender.send(topic, message.toString());
    }
}