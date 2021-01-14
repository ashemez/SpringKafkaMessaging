package com.endpoint.SpringKafkaMessaging.auth.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivationResponse {
	
	private String mobile;
	
	private String activationCode;
	
}
