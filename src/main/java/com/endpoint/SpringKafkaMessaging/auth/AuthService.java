package com.endpoint.SpringKafkaMessaging.auth;

import com.endpoint.SpringKafkaMessaging.persistent.model.AccessToken;

public interface AuthService {
    void putAccessToken(String accessToken, Long userId);
    Long loginWithAccessToken(String code);
    AccessToken getAccesToken(Long userId);
    void deleteByUserId(Long userId);
}

