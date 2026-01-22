import React, { useState, useEffect, useRef } from 'react';
import { Card, Form, Button, ListGroup } from 'react-bootstrap';
import api from '../services/locationService';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const CaseChatComponent = ({ caseId, currentUser }) => {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const stompClientRef = useRef(null);

    useEffect(() => {
        if (!caseId) return;

        // Fetch history
        api.get(`/cases/${caseId}/messages`)
            .then(res => setMessages(res.data))
            .catch(err => console.error("Error fetching messages:", err));

        // Connect WebSocket
        const socket = new SockJS('http://localhost:8081/ws');
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                client.subscribe(`/topic/case/${caseId}`, (message) => {
                    const receivedMsg = JSON.parse(message.body);
                    setMessages(prev => [...prev, receivedMsg]);
                });
            },
        });

        client.activate();
        stompClientRef.current = client;

        return () => {
            if (client) client.deactivate();
        };
    }, [caseId]);

    const handleSendMessage = () => {
        if (!newMessage.trim() || !stompClientRef.current) return;

        const chatMessage = {
            content: newMessage,
            sender: { role: 'COORDINATOR' }, // Simplified for now, should use real user context
            timestamp: new Date().toISOString()
        };

        stompClientRef.current.publish({
            destination: `/app/chat/${caseId}`,
            body: JSON.stringify(chatMessage)
        });

        setNewMessage('');
    };

    return (
        <Card className="h-100">
            <Card.Header>Case Chat #{caseId}</Card.Header>
            <Card.Body className="d-flex flex-column">
                <div className="flex-grow-1 overflow-auto mb-3" style={{ maxHeight: '300px' }}>
                    <ListGroup variant="flush">
                        {messages.map((msg, idx) => (
                            <ListGroup.Item key={idx}>
                                <strong>{msg.senderRole || 'User'}:</strong> {msg.content}
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                </div>
                <div className="d-flex">
                    <Form.Control
                        type="text"
                        placeholder="Type a message..."
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
                    />
                    <Button variant="primary" className="ms-2" onClick={handleSendMessage}>Send</Button>
                </div>
            </Card.Body>
        </Card>
    );
};

export default CaseChatComponent;
