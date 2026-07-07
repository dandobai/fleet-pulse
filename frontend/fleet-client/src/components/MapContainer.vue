<template>
    <main class="flex-1 relative border border-green-500 shadow-[0_0_10px_rgba(0,255,65,0.2)]">
        <div id="map" class="h-full w-full"></div>
    </main>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, watch } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { MapMarkerManager } from '@/services/mapMarkerManager';

const props = defineProps<{ vehicles: Map<string, any> }>();
let map: L.Map | null = null;
let markerManager: MapMarkerManager;

const zoomTo = (lat: number | null, lng: number | null, isSidebarOpen: boolean) => {
    if (!map || lat === null || lng === null) return;

    const padding = isSidebarOpen ? [320, 0] : [0, 0];

    const currentCenter = map.getCenter();
    if (Math.abs(currentCenter.lat - lat) < 0.0001 && Math.abs(currentCenter.lng - lng) < 0.0001) {
        return;
    }

    map.flyTo([lat, lng], 18, {
        animate: true,
        paddingBottomRight: padding as [number, number]
    } as any);
};

const refreshMap = () => {
    map?.invalidateSize();
};

defineExpose({ zoomTo, refreshMap });

onMounted(() => {
    map = L.map('map').setView([47.4979, 19.0402], 13);
    L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; CartoDB'
    }).addTo(map);
    markerManager = new MapMarkerManager(map);
    markerManager.syncMarkers(props.vehicles);
});

watch(() => props.vehicles, (newVal) => {
    if (markerManager) markerManager.syncMarkers(newVal);
}, { deep: true });

onBeforeUnmount(() => {
    if (map) {
        map.remove();
        map = null;
    }
});
</script>