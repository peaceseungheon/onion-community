package com.onion.backend.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class ChatMessage {

    private String message;
    private Long timestamp;
    private Long userNo;

    public ChatMessage(String message, Long timestamp, Long userNo) {
        this.message = message;
        this.timestamp = timestamp;
        this.userNo = userNo;
    }

}
