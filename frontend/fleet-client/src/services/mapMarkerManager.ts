import L from 'leaflet';
import type { VehicleData } from '@/stores/vehicleStore';

export class MapMarkerManager {
  private markers: Map<string, L.Marker> = new Map();
  private map: L.Map;

  private matrixIcon = L.divIcon({
    className: 'matrix-marker',
    html: '<div class="matrix-pin"></div>',
    iconSize: [12, 12],
    iconAnchor: [6, 6]
  });

  constructor(map: L.Map) {
    this.map = map;
  }

  updateMarker(vehicle: VehicleData) {
    const existing = this.markers.get(vehicle.id);
    if (existing) {
      existing.setLatLng([vehicle.lat, vehicle.lng]);
    } else {
      const marker = L.marker([vehicle.lat, vehicle.lng], { 
        icon: this.matrixIcon 
      }).addTo(this.map);
      
      this.markers.set(vehicle.id, marker);
    }
  }

  removeMarker(vehicleId: string) {
    const marker = this.markers.get(vehicleId);
    if (marker) {
      this.map.removeLayer(marker);
      this.markers.delete(vehicleId);
    }
  }

  syncMarkers(currentVehicles: Map<string, VehicleData>) {
    this.markers.forEach((_, id) => {
      if (!currentVehicles.has(id)) this.removeMarker(id);
    });
    currentVehicles.forEach(v => this.updateMarker(v));
  }
}