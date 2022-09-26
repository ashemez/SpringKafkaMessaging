package com.endpoint.SpringKafkaMessaging.websocket;

import com.endpoint.SpringKafkaMessaging.cache.respository.CacheRepository;
import com.endpoint.SpringKafkaMessaging.persistent.model.User;
import com.endpoint.SpringKafkaMessaging.persistent.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WSHandshakeInterceptor implements HandshakeInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(WSHandshakeInterceptor.class);

    private CacheRepository cacheRepository;
    private UserRepository userRepository;

    public WSHandshakeInterceptor(CacheRepository cacheRepository, UserRepository userRepository) {
        this.cacheRepository = cacheRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {

        LOG.info("hand shaking for ws session url  " + serverHttpRequest.getURI().getPath() + " ? " + serverHttpRequest.getURI().getQuery());
        String parameters[] = serverHttpRequest.getURI().getQuery().split("=");

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

            LOG.info("Handshake found userId: " + senderUserId);

            if (senderUserId == 0L) {
                LOG.info("Handshake failed: user not found for given accessToken");
                map.put("CUSTOM-HEADER", "Handshake failed: user not found for given accessToken");
                return false;
            }

            map.put("CUSTOM-HEADER", "Handshake successful");
            LOG.info("Handshake successful");
            return true;
        }

        map.put("CUSTOM-HEADER", "Handshake failed: accessToken not found");
        LOG.info("Handshake failed: accessToken not found");
        return false;

    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Exception e) {

    }
}
