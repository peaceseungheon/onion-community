package com.onion.backend.controller;

import com.onion.backend.dto.chat.ChatMessage;
import java.util.Date;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ChatMessage greeting(ChatMessage chatMessage) throws Exception {
        return chatMessage;
    }

}
