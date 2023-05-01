package com.endpoint.SpringKafkaMessaging.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketPool {

    public static Map<Long, Set<WebSocketSession>> websockets = new ConcurrentHashMap<>();

}
