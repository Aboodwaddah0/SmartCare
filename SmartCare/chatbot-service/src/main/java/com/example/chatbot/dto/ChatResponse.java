package com.example.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponse {
	private String response;

	public ChatResponse(String response) {
		this.response = response;
	}
}
