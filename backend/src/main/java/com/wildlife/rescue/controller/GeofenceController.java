package com.wildlife.rescue.controller;

import com.wildlife.rescue.model.Geofence;
import com.wildlife.rescue.repository.GeofenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geofences")
public class GeofenceController {

    @Autowired
    private GeofenceRepository geofenceRepository;

    @GetMapping
    public List<Geofence> getAllGeofences() {
        return geofenceRepository.findAll();
    }

    @PostMapping
    public Geofence createGeofence(@RequestBody Geofence geofence) {
        return geofenceRepository.save(geofence);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGeofence(@PathVariable Long id) {
        geofenceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
