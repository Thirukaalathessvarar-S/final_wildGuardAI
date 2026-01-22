import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import CoordinatorDashboard from './pages/CoordinatorDashboard';
import VetDashboard from './pages/VetDashboard';
import { Navbar, Container, Nav } from 'react-bootstrap';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar bg="dark" variant="dark" expand="lg">
            <Container>
                <Navbar.Brand as={Link} to="/">WildGuard AI</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/">Coordinator</Nav.Link>
                        <Nav.Link as={Link} to="/vet">Vet Mode</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        
        <Routes>
          <Route path="/" element={<CoordinatorDashboard />} />
          <Route path="/vet" element={<VetDashboard />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
