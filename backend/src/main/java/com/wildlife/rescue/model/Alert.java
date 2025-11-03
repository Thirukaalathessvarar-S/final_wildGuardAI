package com.wildlife.rescue.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Alert {
    private String id;
    private String animalType;
    private String location;
    private String description;
    private LocalDateTime timestamp;
    private String status; // e.g., "new", "assigned", "resolved"
    private String assignedTo; // User ID of the assigned officer/volunteer
    private String senderRole; // e.g., "admin", "vet", "coordinator"
    private String message;
}
