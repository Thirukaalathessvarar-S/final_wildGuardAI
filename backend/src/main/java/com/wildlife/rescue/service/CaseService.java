package com.wildlife.rescue.service;

import com.wildlife.rescue.model.Case;

import com.wildlife.rescue.model.User;

import com.wildlife.rescue.repository.CaseRepository;

import com.wildlife.rescue.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;



import java.util.List;

import java.util.Optional;



@Service

public class CaseService {



    @Autowired

    private CaseRepository caseRepository;



    @Autowired

    private UserRepository userRepository;



    @Autowired

    private SimpMessagingTemplate messagingTemplate;



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



    



        @Transactional



        public Case assignVetToCase(Long caseId, Long vetId) {



            Case rescueCase = caseRepository.findById(caseId)



                    .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + caseId));



            User vet = userRepository.findById(vetId)



                    .orElseThrow(() -> new IllegalArgumentException("Vet not found with id: " + vetId));



    



            if (!"vet".equals(vet.getRole())) {



                throw new IllegalArgumentException("User is not a vet");



            }



    



            if (!vet.isAvailable()) {



                throw new IllegalStateException("Vet is not available");



            }



    



            rescueCase.setAssignedVet(vet);



            vet.setAvailable(false);



    



            userRepository.save(vet);



            Case updatedCase = caseRepository.save(rescueCase);



    



            messagingTemplate.convertAndSend("/topic/cases/updated", updatedCase);



    



            return updatedCase;



        }



    }