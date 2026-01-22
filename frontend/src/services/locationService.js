import axios from 'axios';

const API_URL = 'http://localhost:8081/api';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const getCases = () => api.get('/cases');
export const createCase = (caseData) => api.post('/cases', caseData);
export const getGeofences = () => api.get('/geofences');
export const getAvailableVets = () => api.get('/users/vets/available');
export const getNearestVets = (caseId) => api.get(`/cases/${caseId}/nearest-vets`);
export const updateLocation = (userId, lat, lng) => api.put(`/users/${userId}/location`, { latitude: lat, longitude: lng });

export default api;
