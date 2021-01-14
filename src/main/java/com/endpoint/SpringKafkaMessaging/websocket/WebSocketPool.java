package com.endpoint.SpringKafkaMessaging.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebSocketPool {

    public static Map<Long, Set<WebSocketSession>> websockets = new HashMap<>();

}
