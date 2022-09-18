package com.endpoint.SpringKafkaMessaging.message.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {
	
	private String accessToken;
	
	private Long sendTo;

	private String msg;
	
}
