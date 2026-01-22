package com.wildlife.rescue.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CaseDTO {
    private Long id;
    private String animalType;
    private String location;
    private Double latitude;
    private Double longitude;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
