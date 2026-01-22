import React, { useEffect, useState } from 'react';
import { getCases, createCase, getGeofences, getAvailableVets } from '../services/locationService';
import MapComponent from '../components/MapComponent';
import CaseChatComponent from '../components/CaseChatComponent';
import { Container, Row, Col, Card, ListGroup, Badge, Form, Button, Modal } from 'react-bootstrap';

const CoordinatorDashboard = () => {
    const [cases, setCases] = useState([]);
    const [vets, setVets] = useState([]);
    const [geofences, setGeofences] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [selectedCase, setSelectedCase] = useState(null); // For Chat & Details
    const [newCase, setNewCase] = useState({ animalType: '', description: '', location: '', latitude: '', longitude: '' });

    const fetchData = async () => {
        try {
            const caseData = await getCases();
            setCases(caseData.data);
            
            const vetData = await getAvailableVets();
            setVets(vetData.data);

            const geoData = await getGeofences();
            setGeofences(geoData.data);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    useEffect(() => {
        fetchData();
        const interval = setInterval(fetchData, 10000); // Poll every 10s
        return () => clearInterval(interval);
    }, []);

    const handleCreateCase = async () => {
        try {
            await createCase(newCase);
            setShowCreateModal(false);
            setNewCase({ animalType: '', description: '', location: '', latitude: '', longitude: '' });
            fetchData(); // Refresh list
        } catch (error) {
            console.error("Error creating case:", error);
            alert("Failed to create case");
        }
    };

    return (
        <Container fluid className="p-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Coordinator Dashboard</h2>
                <Button variant="primary" onClick={() => setShowCreateModal(true)}>+ New Rescue Case</Button>
            </div>
            
            <Row>
                <Col md={8}>
                    <Card>
                        <Card.Header>Live Map</Card.Header>
                        <Card.Body>
                            <MapComponent cases={cases} vets={vets} geofences={geofences} />
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card className="mb-3">
                        <Card.Header>Active Cases (Click to Chat)</Card.Header>
                        <ListGroup variant="flush" style={{maxHeight: '400px', overflowY: 'auto'}}>
                            {cases.map(c => (
                                <ListGroup.Item action key={c.id} onClick={() => setSelectedCase(c)}>
                                    <div className="d-flex justify-content-between">
                                        <strong>{c.animalType}</strong>
                                        <Badge bg={c.status === 'RESOLVED' ? 'success' : 'warning'}>{c.status || 'NEW'}</Badge>
                                    </div>
                                    <small className="text-muted">{c.description}</small>
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                    </Card>
                    <Card>
                        <Card.Header>Available Vets</Card.Header>
                        <ListGroup variant="flush">
                            {vets.map(v => (
                                <ListGroup.Item key={v.id} className="d-flex justify-content-between align-items-center">
                                    {v.username}
                                    <Badge bg="success">Available</Badge>
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                    </Card>
                </Col>
            </Row>

            {/* Create Case Modal */}
            <Modal show={showCreateModal} onHide={() => setShowCreateModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Report New Rescue Case</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Animal Type</Form.Label>
                            <Form.Control type="text" placeholder="e.g. Elephant" 
                                value={newCase.animalType} onChange={(e) => setNewCase({...newCase, animalType: e.target.value})} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Description</Form.Label>
                            <Form.Control as="textarea" rows={3} 
                                value={newCase.description} onChange={(e) => setNewCase({...newCase, description: e.target.value})} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Location Description</Form.Label>
                            <Form.Control type="text" placeholder="e.g. Near the river" 
                                value={newCase.location} onChange={(e) => setNewCase({...newCase, location: e.target.value})} />
                        </Form.Group>
                        <Row>
                            <Col>
                                <Form.Group className="mb-3">
                                    <Form.Label>Latitude</Form.Label>
                                    <Form.Control type="number" step="any"
                                        value={newCase.latitude} onChange={(e) => setNewCase({...newCase, latitude: e.target.value})} />
                                </Form.Group>
                            </Col>
                            <Col>
                                <Form.Group className="mb-3">
                                    <Form.Label>Longitude</Form.Label>
                                    <Form.Control type="number" step="any"
                                        value={newCase.longitude} onChange={(e) => setNewCase({...newCase, longitude: e.target.value})} />
                                </Form.Group>
                            </Col>
                        </Row>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowCreateModal(false)}>Close</Button>
                    <Button variant="primary" onClick={handleCreateCase}>Submit Report</Button>
                </Modal.Footer>
            </Modal>

            {/* Case Details & Chat Modal */}
            <Modal show={!!selectedCase} onHide={() => setSelectedCase(null)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>Case Details: {selectedCase?.animalType}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Row>
                        <Col md={5}>
                            <h5>Info</h5>
                            <p><strong>Status:</strong> {selectedCase?.status}</p>
                            <p><strong>Location:</strong> {selectedCase?.location}</p>
                            <p><strong>Description:</strong> {selectedCase?.description}</p>
                            <hr />
                            <h5>Nearest Vets</h5>
                            <Button variant="outline-info" size="sm">Find Nearest Vets</Button>
                        </Col>
                        <Col md={7}>
                            {selectedCase && <CaseChatComponent caseId={selectedCase.id} />}
                        </Col>
                    </Row>
                </Modal.Body>
            </Modal>
        </Container>
    );
};

export default CoordinatorDashboard;
