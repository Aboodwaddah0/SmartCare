package com.example.SmartCare.chatbot;


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
