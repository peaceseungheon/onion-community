package com.onion.backend.controller;

import com.onion.backend.dto.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;

    @MessageMapping("/{roomId}")
    public void greeting(@DestinationVariable("roomId") String roomId, ChatMessage chatMessage) {
        System.out.println("roomId: " + roomId);
        redisTemplate.convertAndSend("/sub/1", chatMessage);
    }

}
