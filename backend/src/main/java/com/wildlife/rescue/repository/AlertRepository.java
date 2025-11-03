package com.wildlife.rescue.repository;

import com.wildlife.rescue.model.Alert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AlertRepository {
    private final ConcurrentHashMap<String, Alert> alerts = new ConcurrentHashMap<>();

    public Alert save(Alert alert) {
        alerts.put(alert.getId(), alert);
        return alert;
    }

    public Optional<Alert> findById(String id) {
        return Optional.ofNullable(alerts.get(id));
    }

    public List<Alert> findAll() {
        return new ArrayList<>(alerts.values());
    }

    public void deleteById(String id) {
        alerts.remove(id);
    }
}
