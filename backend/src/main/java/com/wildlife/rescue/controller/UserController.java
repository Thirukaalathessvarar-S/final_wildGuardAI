package com.wildlife.rescue.controller;

import com.wildlife.rescue.model.User;
import com.wildlife.rescue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private com.wildlife.rescue.service.LocationService locationService;

    @GetMapping("/vets/available")
    public List<User> getAvailableVets() {
        return userService.getAvailableVets();
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/location")
    public org.springframework.http.ResponseEntity<?> updateLocation(@org.springframework.web.bind.annotation.PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody LocationUpdateRequest request) {
        locationService.updateUserLocation(id, request.getLatitude(), request.getLongitude());
        return org.springframework.http.ResponseEntity.ok().build();
    }

    @lombok.Data
    public static class LocationUpdateRequest {
        private double latitude;
        private double longitude;
    }
}
