package com.wildlife.rescue.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // e.g., "COORDINATOR", "VET", "ADMIN"

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean available = true;

    private Double latitude;

    private Double longitude;

    private java.time.LocalDateTime lastLocationUpdate;
}
