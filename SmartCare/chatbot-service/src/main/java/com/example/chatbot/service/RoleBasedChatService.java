package com.example.chatbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoleBasedChatService {

	private final ChatClient openAiChatClient;
	private final ChatClient anthropicChatClient;

	public RoleBasedChatService(
			@Qualifier("openAiChatClient") ChatClient openAiChatClient,
			@Qualifier("anthropicChatClient") ChatClient anthropicChatClient) {
		this.openAiChatClient = openAiChatClient;
		this.anthropicChatClient = anthropicChatClient;
	}

	public String chat(String message, String role) {
		ChatClient client = selectClientByRole(role);

		return client.prompt()
				.system(buildSystemPrompt(role))
				.user(message)
				.call()
				.content();
	}

	private ChatClient selectClientByRole(String role) {
		return switch (role.toUpperCase()) {
			case "DOCTOR" -> openAiChatClient;
			case "PATIENT" -> anthropicChatClient;
			default -> anthropicChatClient;
		};
	}

	private String buildSystemPrompt(String role) {
		return switch (role.toUpperCase()) {
			case "DOCTOR" ->
					"You are a medical assistant helping doctors. Provide clinical insights, suggest treatment options, and help with medical decision-making. Always maintain professional medical standards.";
			case "PATIENT" ->
					"You are a friendly healthcare assistant helping patients. Explain medical information in simple terms, provide general health guidance, and always remind users to consult their doctor for specific medical advice.";
			default ->
					"You are a helpful healthcare assistant. Provide general health information and always remind users to consult a healthcare professional for medical advice.";
		};
	}
}
