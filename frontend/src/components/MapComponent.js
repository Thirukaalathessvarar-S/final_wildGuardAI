import React from 'react';
import { MapContainer, TileLayer, Marker, Popup, Circle } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

// Fix for default marker icon missing in Leaflet with React
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow,
    iconSize: [25, 41],
    iconAnchor: [12, 41]
});

L.Marker.prototype.options.icon = DefaultIcon;

const MapComponent = ({ cases, vets, geofences, center = [51.505, -0.09], zoom = 13 }) => {
    return (
        <MapContainer center={center} zoom={zoom} style={{ height: '500px', width: '100%' }}>
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            
            {/* Render Geofences */}
            {geofences && geofences.map(geo => (
                <Circle 
                    key={geo.id}
                    center={[geo.latitude, geo.longitude]}
                    radius={geo.radius}
                    pathOptions={{ color: geo.type === 'DANGER_ZONE' ? 'red' : 'green', fillColor: geo.type === 'DANGER_ZONE' ? 'red' : 'green' }}
                >
                    <Popup>
                        <strong>{geo.name}</strong> <br/> {geo.description}
                    </Popup>
                </Circle>
            ))}

            {/* Render Cases */}
            {cases && cases.map(c => (
                c.latitude && c.longitude && (
                    <Marker key={c.id} position={[c.latitude, c.longitude]}>
                        <Popup>
                            <strong>{c.animalType}</strong> <br/>
                            {c.description} <br/>
                            Status: {c.status}
                        </Popup>
                    </Marker>
                )
            ))}

            {/* Render Vets */}
            {vets && vets.map(v => (
                v.latitude && v.longitude && (
                    <Marker key={v.id} position={[v.latitude, v.longitude]} opacity={0.7}>
                        <Popup>
                            <strong>Vet: {v.username}</strong> <br/>
                            Status: {v.available ? 'Available' : 'Busy'}
                        </Popup>
                    </Marker>
                )
            ))}
        </MapContainer>
    );
};

export default MapComponent;
