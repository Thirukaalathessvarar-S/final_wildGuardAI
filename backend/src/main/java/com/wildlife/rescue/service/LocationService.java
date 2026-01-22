package com.wildlife.rescue.service;

import com.wildlife.rescue.model.Geofence;
import com.wildlife.rescue.model.User;
import com.wildlife.rescue.repository.GeofenceRepository;
import com.wildlife.rescue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeofenceRepository geofenceRepository;

    private static final double EARTH_RADIUS_KM = 6371.0;

    public void updateUserLocation(Long userId, double latitude, double longitude) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        user.setLastLocationUpdate(LocalDateTime.now());
        userRepository.save(user);
    }

    public List<User> findNearestVets(double lat, double lon, int limit) {
        List<User> availableVets = userRepository.findByRoleAndAvailable("vet", true);
        
        return availableVets.stream()
                .filter(v -> v.getLatitude() != null && v.getLongitude() != null)
                .sorted(Comparator.comparingDouble(v -> calculateDistance(lat, lon, v.getLatitude(), v.getLongitude())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Optional<User> findNearestVet(double lat, double lon) {
        List<User> nearest = findNearestVets(lat, lon, 1);
        return nearest.isEmpty() ? Optional.empty() : Optional.of(nearest.get(0));
    }

    public List<Geofence> checkGeofences(double lat, double lon) {
        List<Geofence> allGeofences = geofenceRepository.findAll();
        return allGeofences.stream()
                .filter(g -> calculateDistance(lat, lon, g.getLatitude(), g.getLongitude()) * 1000 <= g.getRadius())
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
