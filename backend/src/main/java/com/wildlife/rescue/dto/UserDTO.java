package com.wildlife.rescue.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private Double latitude;
    private Double longitude;
}
