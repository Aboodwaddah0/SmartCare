package com.example.chatbot.controller;

import com.example.chatbot.dto.ChatResponse;
import com.example.chatbot.service.RoleBasedChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v2/chat")
public class ChatController {

	private final RoleBasedChatService chatService;

	public ChatController(RoleBasedChatService chatService) {
		this.chatService = chatService;
	}

	@PostMapping
	public ChatResponse chat(@RequestBody String message, HttpServletRequest request) {
		String role = request.getHeader("X-User-Roles");

		if (role == null) {
			role = "PATIENT";
		}

		String cleanedRole = role.replace("ROLE_", "");

		String response = chatService.chat(message, cleanedRole);
		return new ChatResponse(response);
	}
}
