package com.wildlife.rescue.mapper;

import com.wildlife.rescue.dto.ChatMessageDTO;
import com.wildlife.rescue.dto.UserDTO;
import com.wildlife.rescue.model.ChatMessage;
import com.wildlife.rescue.model.User;

public class ChatMessageMapper {

    public static ChatMessageDTO toDTO(ChatMessage chatMessage) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatMessage.getId());
        dto.setContent(chatMessage.getContent());
        dto.setImageUrl(chatMessage.getImageUrl());
        dto.setTimestamp(chatMessage.getTimestamp());
        dto.setSender(toDTO(chatMessage.getSender()));
        return dto;
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }
}
