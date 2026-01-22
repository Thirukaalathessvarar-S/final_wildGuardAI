package com.wildlife.rescue.controller;

import com.wildlife.rescue.dto.CaseDTO;
import com.wildlife.rescue.dto.ChatMessageDTO;
import com.wildlife.rescue.mapper.CaseMapper;
import com.wildlife.rescue.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private com.wildlife.rescue.service.LocationService locationService;

    @GetMapping
    public List<CaseDTO> getAllCases() {
        return caseService.getAllCases().stream()
                .map(CaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public CaseDTO createCase(@RequestBody com.wildlife.rescue.model.Case newCase) {
        return CaseMapper.toDTO(caseService.createCase(newCase));
    }

    @GetMapping("/{caseId}/messages")
    public List<ChatMessageDTO> getChatMessagesForCase(@PathVariable Long caseId) {
        return caseService.getCaseById(caseId)
                .map(com.wildlife.rescue.model.Case::getChatMessages)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + caseId))
                .stream()
                .map(com.wildlife.rescue.mapper.ChatMessageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{caseId}/assign-vet/{vetId}")
    public CaseDTO assignVet(@PathVariable Long caseId, @PathVariable Long vetId) {
        com.wildlife.rescue.model.Case updatedCase = caseService.assignVetToCase(caseId, vetId);
        return CaseMapper.toDTO(updatedCase);
    }

    @GetMapping("/{caseId}/nearest-vets")
    public List<com.wildlife.rescue.model.User> getNearestVets(@PathVariable Long caseId) {
        return caseService.getCaseById(caseId)
                .map(c -> {
                    if (c.getLatitude() == null || c.getLongitude() == null) {
                        return java.util.Collections.<com.wildlife.rescue.model.User>emptyList();
                    }
                    return locationService.findNearestVets(c.getLatitude(), c.getLongitude(), 5);
                })
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + caseId));
    }
}
