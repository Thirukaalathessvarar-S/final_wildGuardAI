package com.wildlife.rescue.controller;

import com.wildlife.rescue.model.Case;
import com.wildlife.rescue.model.ChatMessage;
import com.wildlife.rescue.service.CaseService;
import com.wildlife.rescue.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private com.wildlife.rescue.repository.UserRepository userRepository;

    @MessageMapping("/case/create")
    public void createCase(Case newCase) {
        Case createdCase = caseService.createCase(newCase);
        messagingTemplate.convertAndSend("/topic/cases", createdCase);
    }

    @MessageMapping("/chat/{caseId}")
    public void sendChatMessage(@DestinationVariable Long caseId, ChatMessage chatMessage) {
        Case rescueCase = caseService.getCaseById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + caseId));
        chatMessage.setRescueCase(rescueCase);

        String role = chatMessage.getSender().getRole();
        String username = role.toLowerCase() + "_user";

        com.wildlife.rescue.model.User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException(username + " not found"));
        chatMessage.setSender(sender);
        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/case/" + caseId, savedMessage);
    }

    @MessageMapping("/case/delete/{caseId}")
    public void deleteCase(@DestinationVariable Long caseId) {
        caseService.deleteCase(caseId);
        messagingTemplate.convertAndSend("/topic/cases/deleted", caseId);
    }
}
