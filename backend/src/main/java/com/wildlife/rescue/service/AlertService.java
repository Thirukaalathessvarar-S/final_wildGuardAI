package com.wildlife.rescue.service;

import com.wildlife.rescue.model.Alert;
import com.wildlife.rescue.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Alert createAlert(Alert alert) {
        alert.setId(UUID.randomUUID().toString());
        alert.setTimestamp(LocalDateTime.now());
        alert.setStatus("new");
        Alert newAlert = alertRepository.save(alert);
        messagingTemplate.convertAndSend("/topic/alerts", newAlert);
        return newAlert;
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Optional<Alert> getAlertById(String id) {
        return alertRepository.findById(id);
    }

    public Alert updateAlert(String id, Alert updatedAlert) {
        return alertRepository.findById(id).map(alert -> {
            alert.setAnimalType(updatedAlert.getAnimalType());
            alert.setLocation(updatedAlert.getLocation());
            alert.setDescription(updatedAlert.getDescription());
            alert.setStatus(updatedAlert.getStatus());
            alert.setAssignedTo(updatedAlert.getAssignedTo());
            return alertRepository.save(alert);
        }).orElseThrow(() -> new RuntimeException("Alert not found with id " + id));
    }

    public void deleteAlert(String id) {
        alertRepository.deleteById(id);
    }
}
