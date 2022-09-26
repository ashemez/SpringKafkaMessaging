package com.endpoint.SpringKafkaMessaging.auth.controller;

import com.endpoint.SpringKafkaMessaging.persistent.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {
	
	private List<Contact> contacts;

}

