package com.wildlife.rescue.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
    private Long id;
    private UserDTO sender;
    private String content;
    private String imageUrl;
    private LocalDateTime timestamp;
}
