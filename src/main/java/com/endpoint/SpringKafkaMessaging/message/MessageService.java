package com.endpoint.SpringKafkaMessaging.message;

import com.endpoint.SpringKafkaMessaging.persistent.model.Message;

import java.util.List;

public interface MessageService {

    public void sendMessage(String accessToken, Long sendTo, String msg, String topic);

    List<Message> getMessageHistory(Long fromUserId, Long toUserId);
}
