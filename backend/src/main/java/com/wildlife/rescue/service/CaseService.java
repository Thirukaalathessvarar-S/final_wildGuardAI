package com.wildlife.rescue.service;

import com.wildlife.rescue.model.Case;
import com.wildlife.rescue.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    public Case createCase(Case newCase) {
        return caseRepository.save(newCase);
    }

    public List<Case> getAllCases() {
        return caseRepository.findAll();
    }

    public Optional<Case> getCaseById(Long id) {
        return caseRepository.findById(id);
    }

    public void deleteCase(Long id) {
        caseRepository.deleteById(id);
    }
}