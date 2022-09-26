package com.endpoint.SpringKafkaMessaging.websocket;

import com.endpoint.SpringKafkaMessaging.message.broker.MessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);

    @Override
    public void addSessionToPool(Long userId, WebSocketSession session) {

        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions != null) {
            userSessions.add(session);
            WebSocketPool.websockets.put(userId, userSessions);
        } else {
            Set<WebSocketSession> newUserSessions = new HashSet<>();
            newUserSessions.add(session);
            WebSocketPool.websockets.put(userId, newUserSessions);
        }

    }

    @Override
    public void sendMessageToUser(Long userId, String message) throws IOException {

        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions == null) {
            LOG.info("No websocket sessions found for given destination userID");
            return;
        }

        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : userSessions) {
            LOG.info("Sending websocket message " + textMessage.getPayload());
            session.sendMessage(textMessage);
        }

    }

    @Override
    public void removeFromSessionToPool(Long userId, WebSocketSession session) {
        Set<WebSocketSession> userSessions = WebSocketPool.websockets.get(userId);

        if (userSessions != null) {
            LOG.info("Sessions found for userId " + userId);
            for (WebSocketSession sessionItem : userSessions) {
                if (sessionItem.equals(session)) {
                    LOG.info("Removing ession for userId " + sessionItem.hashCode());
                    userSessions.remove(session);
                } else {
                    LOG.info("This session is not equal hmm: " + sessionItem.hashCode() + " <> " + session.hashCode());
                }
            }
        }
        WebSocketPool.websockets.put(userId, userSessions);
    }
}
