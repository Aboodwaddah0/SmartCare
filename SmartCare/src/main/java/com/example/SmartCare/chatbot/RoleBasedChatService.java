package com.example.SmartCare.chatbot;

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
}