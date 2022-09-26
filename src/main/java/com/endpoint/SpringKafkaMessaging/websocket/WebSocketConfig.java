package com.endpoint.SpringKafkaMessaging.websocket;

import com.endpoint.SpringKafkaMessaging.cache.respository.CacheRepository;
import com.endpoint.SpringKafkaMessaging.persistent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  {

    @Bean
    public WebSocketHandler myMessageHandler() {
        return new WebSocketHandler();
    }

    @Autowired
    CacheRepository cacheRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myMessageHandler(), "/messaging").setAllowedOrigins("*")
                .addInterceptors(new WSHandshakeInterceptor(cacheRepository, userRepository));
    }

}
