import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import './App.css';

function App() {
  const [role, setRole] = useState('coordinator');
  const [cases, setCases] = useState([]);
  const [selectedCase, setSelectedCase] = useState(null);
  const [chatMessages, setChatMessages] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [newCaseData, setNewCaseData] = useState({ animalType: '', location: '', description: '' });
  const [chatInput, setChatInput] = useState('');
  const [availableVets, setAvailableVets] = useState([]);
  const [selectedVet, setSelectedVet] = useState('');

  useEffect(() => {
    // Fetch initial cases
    fetch("http://localhost:8081/api/cases")
      .then((response) => response.json())
      .then((data) => setCases(data));

    const socket = new SockJS('http://localhost:8081/ws');
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log('Connected to WebSocket');
        client.subscribe('/topic/cases', (message) => {
          const newCase = JSON.parse(message.body);
          setCases((prevCases) => [...prevCases, newCase]);
        });
        client.subscribe('/topic/cases/deleted', (message) => {
          const deletedCaseId = JSON.parse(message.body);
          setCases((prevCases) => prevCases.filter((c) => c.id !== deletedCaseId));
        });
        client.subscribe('/topic/cases/updated', (message) => {
            const updatedCase = JSON.parse(message.body);
            setCases((prevCases) =>
                prevCases.map((c) => (c.id === updatedCase.id ? updatedCase : c))
            );
            if (selectedCase && selectedCase.id === updatedCase.id) {
                setSelectedCase(updatedCase);
            }
            // Refetch available vets
            fetch("http://localhost:8081/api/users/vets/available")
                .then((response) => response.json())
                .then((data) => setAvailableVets(data));
        });
      },
    });

    client.activate();
    setStompClient(client);

    // Fetch available vets
    fetch("http://localhost:8081/api/users/vets/available")
      .then((response) => response.json())
      .then((data) => setAvailableVets(data));

    return () => {
      if (client) {
        client.deactivate();
      }
    };
  }, []);

  useEffect(() => {
    if (selectedCase && stompClient) {
      setChatMessages([]); // Clear previous messages

      // Fetch chat history
      fetch(`http://localhost:8081/api/cases/${selectedCase.id}/messages`)
        .then((response) => response.json())
        .then((data) => {
          if (Array.isArray(data)) {
            setChatMessages(data);
          } else {
            console.error("Fetched chat messages are not an array:", data);
          }
        })
        .catch((error) => console.error("Error fetching chat messages:", error));

      const subscription = stompClient.subscribe(`/topic/case/${selectedCase.id}`, (message) => {
        const chatMessage = JSON.parse(message.body);
        setChatMessages((prevMessages) => [...prevMessages, chatMessage]);
      });

      return () => {
        subscription.unsubscribe();
      };
    }
  }, [selectedCase, stompClient]);

  const createCase = () => {
    if (stompClient && newCaseData.animalType && newCaseData.location && newCaseData.description) {
      stompClient.publish({ destination: '/app/case/create', body: JSON.stringify(newCaseData) });
      setNewCaseData({ animalType: '', location: '', description: '' });
    }
  };

  const deleteCase = (caseId) => {
    if (stompClient) {
      stompClient.publish({ destination: `/app/case/delete/${caseId}` });
    }
  };

  const sendChatMessage = (imageUrl = null) => {
    if (stompClient && selectedCase && (chatInput || imageUrl)) {
      const chatMessage = { content: chatInput, sender: { role }, imageUrl };
      stompClient.publish({ destination: `/app/chat/${selectedCase.id}`, body: JSON.stringify(chatMessage) });
      setChatInput('');
    }
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append("file", file);

      fetch("http://localhost:8081/api/files/upload", {
        method: "POST",
        body: formData,
      })
        .then((response) => response.text())
        .then((imageUrl) => {
          sendChatMessage(imageUrl);
        })
        .catch((error) => console.error("Error uploading image:", error));
    }
  };

  const assignVet = () => {
    if (selectedCase && selectedVet) {
      fetch(`http://localhost:8081/api/cases/${selectedCase.id}/assign-vet/${selectedVet}`, {
        method: 'POST',
      })
        .then((response) => response.json())
        .then((updatedCase) => {
          // The WebSocket subscription will handle the updates
        });
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Wildlife Rescue Coordination</h1>
        <div className="role-selector">
          <span>Select your role: </span>
          <select value={role} onChange={(e) => setRole(e.target.value)}>
            <option value="coordinator">Coordinator</option>
            <option value="vet">Vet</option>
            <option value="admin">Admin</option>
          </select>
        </div>
      </header>

      <div className="main-container">
        <div className="case-creation-section">
          <h2>Create New Case</h2>
          <input
            type="text"
            placeholder="Animal Type"
            value={newCaseData.animalType}
            onChange={(e) => setNewCaseData({ ...newCaseData, animalType: e.target.value })}
          />
          <input
            type="text"
            placeholder="Location"
            value={newCaseData.location}
            onChange={(e) => setNewCaseData({ ...newCaseData, location: e.target.value })}
          />
          <input
            type="text"
            placeholder="Description"
            value={newCaseData.description}
            onChange={(e) => setNewCaseData({ ...newCaseData, description: e.target.value })}
          />
          <button onClick={createCase}>Create Case</button>
        </div>

        <div className="case-list-section">
          <h2>Rescue Cases</h2>
          <div className="case-list">
            {cases.map((caseItem) => (
              <div key={caseItem.id} className="case-item">
                <div onClick={() => setSelectedCase(caseItem)}>
                  <p><strong>Animal:</strong> {caseItem.animalType}</p>
                  <p><strong>Location:</strong> {caseItem.location}</p>
                  <p><strong>Status:</strong> {caseItem.status}</p>
                  {caseItem.assignedVet && <p><strong>Assigned Vet:</strong> {caseItem.assignedVet.username}</p>}
                </div>
                <button onClick={() => deleteCase(caseItem.id)}>Delete</button>
              </div>
            ))}
          </div>
        </div>

        {selectedCase && (
          <div className="case-details-section">
            <h2>Case #{selectedCase.id}</h2>
            {selectedCase.assignedVet ? (
              <p><strong>Assigned Vet:</strong> {selectedCase.assignedVet.username}</p>
            ) : (
              (role === 'coordinator' || role === 'admin') && (
                <div className="vet-assignment-section">
                  <h3>Assign a Vet</h3>
                  <select value={selectedVet} onChange={(e) => setSelectedVet(e.target.value)}>
                    <option value="">Select a Vet</option>
                    {availableVets.map((vet) => (
                      <option key={vet.id} value={vet.id}>
                        {vet.username}
                      </option>
                    ))}
                  </select>
                  <button onClick={assignVet}>Assign Vet</button>
                </div>
              )
            )}
            <div className="chat-room">
              <div className="messages-container">
                {Array.isArray(chatMessages) && chatMessages.map((chat, index) => (
                  <div key={index} className={`message chat-message role-${chat.sender.role}`}>
                    <strong>{chat.sender.role}: </strong>
                    {chat.content}
                    {chat.imageUrl && <img src={chat.imageUrl} alt="chat-img" style={{ maxWidth: '200px' }}/>}
                  </div>
                ))}}
              </div>
              <div className="message-input">
                <input
                  type="text"
                  value={chatInput}
                  onChange={(e) => setChatInput(e.target.value)}
                  placeholder="Enter chat message"
                />
                <input type="file" onChange={handleImageUpload} />
                <button onClick={() => sendChatMessage()}>Send</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;