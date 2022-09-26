package com.endpoint.SpringKafkaMessaging.auth.controller;

import javax.validation.Valid;

import com.endpoint.SpringKafkaMessaging.message.MessageServiceImpl;
import com.endpoint.SpringKafkaMessaging.message.dto.request.SendMessageRequest;
import com.endpoint.SpringKafkaMessaging.message.dto.response.SendMessageResponse;
import com.endpoint.SpringKafkaMessaging.persistent.model.AccessToken;
import com.endpoint.SpringKafkaMessaging.persistent.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.endpoint.SpringKafkaMessaging.auth.AuthService;
import com.endpoint.SpringKafkaMessaging.cache.respository.CacheRepository;
import com.endpoint.SpringKafkaMessaging.persistent.repository.UserRepository;
import com.endpoint.SpringKafkaMessaging.util.StringHelper;

import java.util.*;


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

    @CrossOrigin
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
                                .fname(loginRequest.getMobile())
                                .lname(loginRequest.getMobile())
                                .createdAt(Calendar.getInstance().getTime())
                                .build()
                );
                user = userRepository.findByMobile(loginRequest.getMobile());
            }

            userId = user.getUserId();
            AccessToken accessToken = authService.getAccesToken(userId);

            String token = "";
            if (accessToken == null) {
                token = UUID.randomUUID().toString();
                authService.putAccessToken(token, userId);
            } else {
                token = accessToken.getToken();
            }

            return new ResponseEntity<>(
                    LoginResponse.builder()
                            .accessToken(token)
                            .build(),
                    HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getcontacts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getContacts(@Valid @RequestBody ContactRequest activationRequest) {

        int code = StringHelper.generateRandomNumber(6);

        // save the activation code to the cache repository (cached auth token)
        User user = userRepository.findByToken(activationRequest.getAccessToken());

        ContactResponse contactResponse = ContactResponse.builder()
                .contacts(user.getContacts())
                .build();

        return new ResponseEntity<>(
                contactResponse,
                HttpStatus.OK);
    }

}
