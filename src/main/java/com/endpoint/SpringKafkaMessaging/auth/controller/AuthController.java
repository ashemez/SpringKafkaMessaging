package com.endpoint.SpringKafkaMessaging.auth.controller;

import javax.validation.Valid;

import com.endpoint.SpringKafkaMessaging.persistent.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.endpoint.SpringKafkaMessaging.auth.AuthService;
import com.endpoint.SpringKafkaMessaging.cache.respository.CacheRepository;
import com.endpoint.SpringKafkaMessaging.persistent.repository.UserRepository;
import com.endpoint.SpringKafkaMessaging.util.StringHelper;

import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    CacheRepository cacheRepository;
    
    @RequestMapping(value = "/getcode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getCode(@Valid @RequestBody ActivationRequest activationRequest) {
    	
    	int code = StringHelper.generateRandomNumber(6);
    	
    	// save the activation code to the cache repository (cached auth token)
    	cacheRepository.putActivationCode(activationRequest.getMobile(), String.valueOf(code));

    	ActivationResponse activationResponse = ActivationResponse.builder()
                .mobile(activationRequest.getMobile())
                .activationCode(String.valueOf(code))
                .build();

        return new ResponseEntity<>(
                activationResponse,
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        String mobile = cacheRepository.queryMobileActivationCode(loginRequest.getMobile(), loginRequest.getActivationCode());

        if(mobile == null) {
            return new ResponseEntity<>(
                    "Mobile number not found!",
                    HttpStatus.NOT_FOUND);
        } else {
            Long userId = 0L;
            User user = userRepository.findByMobile(loginRequest.getMobile());
            if(user == null) {
                // save user in persistence
                userRepository.save(
                        User.builder()
                        .mobile(loginRequest.getMobile())
                        .build()
                );
                user = userRepository.findByMobile(loginRequest.getMobile());
            }
            userId = user.getUserId();
            String accessToken = UUID.randomUUID().toString();
            authService.putAccessToken(accessToken, userId);

            return new ResponseEntity<>(
                    LoginResponse.builder()
                            .accessToken(accessToken)
                            .build(),
                    HttpStatus.OK);
        }
    }

}
