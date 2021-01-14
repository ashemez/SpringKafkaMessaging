package com.endpoint.SpringKafkaMessaging.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface MessageHandler {

    public void addSessionToPool(Long userId, WebSocketSession session);

    public void sendMessageToUser(Long userId, String message) throws IOException;

    void removeFromSessionToPool(Long userId, WebSocketSession session);
}
