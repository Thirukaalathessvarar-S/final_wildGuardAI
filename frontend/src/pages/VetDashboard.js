import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Alert, Card } from 'react-bootstrap';
import { updateLocation } from '../services/locationService';

const VetDashboard = () => {
    const [userId, setUserId] = useState('');
    const [tracking, setTracking] = useState(false);
    const [location, setLocation] = useState(null);
    const [message, setMessage] = useState('');

    useEffect(() => {
        let watchId;
        if (tracking && userId) {
            if ("geolocation" in navigator) {
                watchId = navigator.geolocation.watchPosition(
                    (position) => {
                        const { latitude, longitude } = position.coords;
                        setLocation({ latitude, longitude });
                        updateLocation(userId, latitude, longitude)
                            .then(() => setMessage(`Location updated: ${latitude}, ${longitude}`))
                            .catch(err => setMessage(`Error updating backend: ${err.message}`));
                    },
                    (error) => {
                        setMessage(`Error getting location: ${error.message}`);
                    },
                    { enableHighAccuracy: true }
                );
            } else {
                setMessage("Geolocation not supported");
            }
        }
        return () => {
            if (watchId) navigator.geolocation.clearWatch(watchId);
        };
    }, [tracking, userId]);

    const handleStart = () => {
        if (!userId) {
            alert("Please enter a User ID first (simulate login)");
            return;
        }
        setTracking(true);
    };

    const handleStop = () => {
        setTracking(false);
        setMessage("Tracking stopped.");
    };

    return (
        <Container className="mt-5">
            <Card>
                <Card.Header>Vet Field Companion</Card.Header>
                <Card.Body>
                    <Form.Group className="mb-3">
                        <Form.Label>Vet User ID (Simulated Login)</Form.Label>
                        <Form.Control 
                            type="number" 
                            placeholder="Enter ID" 
                            value={userId} 
                            onChange={(e) => setUserId(e.target.value)} 
                            disabled={tracking}
                        />
                    </Form.Group>
                    
                    <div className="d-grid gap-2">
                        {!tracking ? (
                            <Button variant="primary" onClick={handleStart}>Start Shift (Track Location)</Button>
                        ) : (
                            <Button variant="danger" onClick={handleStop}>End Shift</Button>
                        )}
                    </div>

                    {message && <Alert variant="info" className="mt-3">{message}</Alert>}
                    
                    {location && (
                        <div className="mt-3">
                            <strong>Current Coords:</strong> {location.latitude.toFixed(4)}, {location.longitude.toFixed(4)}
                        </div>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default VetDashboard;
