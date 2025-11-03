package com.wildlife.rescue.controller;

import com.wildlife.rescue.dto.CaseDTO;
import com.wildlife.rescue.dto.ChatMessageDTO;
import com.wildlife.rescue.mapper.CaseMapper;
import com.wildlife.rescue.mapper.ChatMessageMapper;
import com.wildlife.rescue.model.ChatMessage;
import com.wildlife.rescue.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @GetMapping
    public List<CaseDTO> getAllCases() {
        return caseService.getAllCases().stream()
                .map(CaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{caseId}/messages")
    public List<ChatMessageDTO> getChatMessagesForCase(@PathVariable Long caseId) {
        return caseService.getCaseById(caseId)
                .map(com.wildlife.rescue.model.Case::getChatMessages)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + caseId))
                .stream()
                .map(ChatMessageMapper::toDTO)
                .collect(Collectors.toList());
    }
}
