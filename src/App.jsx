import React, { useState, useMemo } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polyline, useMap } from 'react-leaflet';
import { Package, MapPin, Navigation, RotateCcw, ChevronRight, ExternalLink, Map as MapIcon, X } from 'lucide-react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { locations, roads } from './data';
import { buildGraph, planMultiStopRoute } from './utils';

// Fix for default marker icons in Leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

// Custom Icons
const warehouseIcon = L.divIcon({
  className: 'custom-div-icon',
  html: `<div style="background-color: #2563eb; width: 14px; height: 14px; border-radius: 50%; border: 2px solid white; box-shadow: 0 0 12px rgba(37, 99, 235, 0.4);"></div>`,
  iconSize: [14, 14],
  iconAnchor: [7, 7]
});

const deliveryIcon = L.divIcon({
  className: 'custom-div-icon',
  html: `<div style="background-color: #94a3b8; width: 12px; height: 12px; border-radius: 50%; border: 2px solid white; box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);"></div>`,
  iconSize: [12, 12],
  iconAnchor: [6, 6]
});

const stopIcon = L.divIcon({
  className: 'custom-div-icon',
  html: `<div style="background-color: #f59e0b; width: 12px; height: 12px; border-radius: 50%; border: 2px solid white; box-shadow: 0 0 10px rgba(245, 158, 11, 0.4);"></div>`,
  iconSize: [12, 12],
  iconAnchor: [6, 6]
});

function RouteCenter({ route }) {
  const map = useMap();
  React.useEffect(() => {
    if (route && route.length > 0) {
      const bounds = L.latLngBounds(route.map(r => [r.lat, r.lng]));
      map.fitBounds(bounds, { padding: [50, 50] });
    }
  }, [route, map]);
  return null;
}

export default function App() {
  const [selectedStops, setSelectedStops] = useState([]);
  const [plannedRoute, setPlannedRoute] = useState(null);
  const [roadGeometry, setRoadGeometry] = useState(null);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);

  const warehouse = useMemo(() => locations.find(l => l.isWarehouse), []);
  const graph = useMemo(() => buildGraph(roads), []);

  const fetchRoadPath = async (routeCoords) => {
    setLoading(true);
    try {
      const coordsString = routeCoords.map(p => `${p.lng},${p.lat}`).join(';');
      const response = await fetch(`https://router.project-osrm.org/route/v1/driving/${coordsString}?overview=full&geometries=geojson`);
      const data = await response.json();
      
      if (data.routes && data.routes.length > 0) {
        // OSRM returns coordinates as [lng, lat]
        const flippedCoords = data.routes[0].geometry.coordinates.map(c => [c[1], c[0]]);
        setRoadGeometry(flippedCoords);
      }
    } catch (error) {
      console.error("Error fetching road path:", error);
      // Fallback to straight lines if API fails
      setRoadGeometry(routeCoords.map(p => [p.lat, p.lng]));
    } finally {
      setLoading(false);
    }
  };

  const toggleStop = (name) => {
    if (selectedStops.includes(name)) {
      setSelectedStops(prev => prev.filter(s => s !== name));
    } else {
      setSelectedStops(prev => [...prev, name]);
    }
    setPlannedRoute(null);
    setStats(null);
  };

  const handlePlanRoute = async () => {
    if (selectedStops.length === 0) return;
    const { route, totalDistance } = planMultiStopRoute(graph, warehouse.name, selectedStops);
    
    const routeCoords = route.map(name => locations.find(l => l.name === name));
    setPlannedRoute(routeCoords);
    setStats({ distance: totalDistance, stops: selectedStops.length });
    
    // Fetch the real road geometry
    await fetchRoadPath(routeCoords);
  };

  const reset = () => {
    setSelectedStops([]);
    setPlannedRoute(null);
    setRoadGeometry(null);
    setStats(null);
  };

  const openGoogleMaps = () => {
    if (!plannedRoute) return;
    const baseUrl = "https://www.google.com/maps/dir/";
    const path = plannedRoute.map(p => `${p.lat},${p.lng}`).join('/');
    window.open(baseUrl + path, '_blank');
  };

  return (
    <>
      <div className="sidebar">
        <div className="sidebar-header">
          <h1><Package size={28} color="#0ea5e9" /> Route Optimizer</h1>
        </div>

        <div className="sidebar-content">
          <section>
            <h2 className="section-title">Available Locations</h2>
            <div className="location-grid">
              {locations.filter(l => !l.isWarehouse).map(loc => (
                <button
                  key={loc.name}
                  className={`location-chip ${selectedStops.includes(loc.name) ? 'selected' : ''}`}
                  onClick={() => toggleStop(loc.name)}
                >
                  <MapPin size={14} />
                  {loc.name}
                </button>
              ))}
            </div>
          </section>

          <section>
            <h2 className="section-title">Delivery Queue ({selectedStops.length})</h2>
            <div className="queue-list">
              {selectedStops.length === 0 ? (
                <p style={{ color: '#64748b', fontSize: '0.9rem', textAlign: 'center' }}>No stops selected yet.</p>
              ) : (
                selectedStops.map(stop => (
                  <div key={stop} className="queue-item animate-fade">
                    <span>{stop}</span>
                    <button onClick={() => toggleStop(stop)}><X size={16} /></button>
                  </div>
                ))
              )}
            </div>
          </section>

          {stats && (
            <div className="stats-card animate-fade">
              <p className="section-title" style={{ marginBottom: 4 }}>Total Distance</p>
              <div className="stats-value">{stats.distance.toFixed(1)} KM</div>
              <p style={{ fontSize: '0.8rem', color: '#64748b', marginTop: 4 }}>
                Optimized route for {stats.stops} deliveries
              </p>
            </div>
          )}
        </div>

        <div className="actions">
          <button 
            className="btn btn-primary" 
            onClick={handlePlanRoute}
            disabled={selectedStops.length === 0 || loading}
          >
            <Navigation size={18} className={loading ? 'animate-spin' : ''} />
            {loading ? 'Calculating Roads...' : 'Plan Optimal Route'}
          </button>
          <button className="btn btn-secondary" onClick={reset}>
            <RotateCcw size={18} />
            Reset Planner
          </button>
          {plannedRoute && (
            <button className="btn btn-gmaps animate-fade" onClick={openGoogleMaps}>
              <ExternalLink size={18} />
              Open in G-Maps
            </button>
          )}
        </div>
      </div>

      <div className="map-container">
        <MapContainer center={[11.0183, 76.9644]} zoom={13} zoomControl={false}>
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png"
          />
          
          {/* Warehouse */}
          <Marker position={[warehouse.lat, warehouse.lng]} icon={warehouseIcon}>
            <Popup><b>Warehouse:</b> {warehouse.name}</Popup>
          </Marker>

          {/* All Locations */}
          {locations.filter(l => !l.isWarehouse).map(loc => (
            <Marker 
              key={loc.name} 
              position={[loc.lat, loc.lng]} 
              icon={selectedStops.includes(loc.name) ? stopIcon : deliveryIcon}
            >
              <Popup><b>{loc.name}</b></Popup>
            </Marker>
          ))}

          {/* Planned Route Line (Exact Roads) */}
          {roadGeometry && (
            <>
              <Polyline 
                positions={roadGeometry} 
                color="#2563eb" 
                weight={5} 
                opacity={0.8}
              />
              <RouteCenter route={plannedRoute} />
            </>
          )}
        </MapContainer>
      </div>
    </>
  );
}
