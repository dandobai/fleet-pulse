<template>
  <div id="map"></div>
</template>

<style>
#map { width: 100vw; height: 100vh; }
</style>

<script setup lang="ts">
import { onMounted, watch, ref } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { useVehicleStore } from '@/stores/vehicleStore';

const store = useVehicleStore();
const markers = new Map<string, L.Marker>();
let map: L.Map;

onMounted(() => {
  map = L.map('map').setView([47.4979, 19.0402], 13);
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap'
  }).addTo(map);
  console.log("Térkép központja:", map.getCenter());
});

// Reagálás a store változására
watch(() => store.vehicles, (newVehicles) => {
  console.log("Store frissült, járművek száma:", newVehicles.size);
  newVehicles.forEach((vehicle) => {
    if (markers.has(vehicle.id)) {
      markers.get(vehicle.id)!.setLatLng([vehicle.lat, vehicle.lng]);
    } else {
      const marker = L.marker([vehicle.lat, vehicle.lng]).addTo(map);
      markers.set(vehicle.id, marker);
    }
  });
}, { deep: true });
</script>