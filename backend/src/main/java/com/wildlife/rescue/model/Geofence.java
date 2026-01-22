package com.wildlife.rescue.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "geofences")
public class Geofence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double radius; // Radius in meters

    @Column(nullable = false)
    private String type; // e.g., "DANGER_ZONE", "SAFE_ZONE", "RESTRICTED"
}
