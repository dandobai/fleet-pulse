<template>
    <div class="h-full w-full flex flex-col bg-black font-mono text-green-500 p-2 gap-2">
        <MapHeader :is-visible="isListVisible" @toggle="toggleList" />

        <div :class="isListVisible ? 'flex-1 flex overflow-hidden gap-2' : 'flex-1 flex overflow-hidden gap-0'">
            <MapContainer ref="mapRef" :vehicles="filteredVehicles" />

            <VehicleSidebar :is-visible="isListVisible" :vehicles="vehiclesArray" :notifications="notificationsArray"
                :selected-vehicles="selectedVehicles" :followed-id="followedVehicleId"
                @toggle-selection="toggleSelection" @sub-action="handleSubAction" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useVehicleStore } from '@/stores/vehicleStore';
import { useNotificationStore } from '@/stores/notificationStore';
import MapHeader from '../components/MapHeader.vue';
import MapContainer from '../components/MapContainer.vue';
import VehicleSidebar from '../components/VehicleSidebar.vue';

interface MapContainerInstance {
    zoomTo: (lat: number, lng: number, isSidebarOpen: boolean) => void;
    refreshMap: () => void;
}

const vehicleStore = useVehicleStore();
const notificationStore = useNotificationStore();

const isListVisible = ref(false);
const selectedVehicles = ref<Set<string>>(new Set());
const followedVehicleId = ref<string | null>(null);

const mapRef = ref<MapContainerInstance | null>(null);

const vehiclesArray = computed(() => Array.from(vehicleStore.vehicles.values()));
const notificationsArray = computed(() => notificationStore.notifications);

const filteredVehicles = computed(() => {
    if (selectedVehicles.value.size === 0) return vehicleStore.vehicles;
    const filtered = new Map();
    selectedVehicles.value.forEach(id => {
        if (vehicleStore.vehicles.has(id)) filtered.set(id, vehicleStore.vehicles.get(id));
    });
    return filtered;
});

watch(() => vehicleStore.vehicles, (newVehicles) => {
    if (followedVehicleId.value && mapRef.value) {
        const v = newVehicles.get(followedVehicleId.value);
        if (v) {
            mapRef.value.zoomTo(v.lat, v.lng, isListVisible.value);
        }
    }
}, { deep: true });

const toggleList = () => {
    isListVisible.value = !isListVisible.value;
    setTimeout(() => {
        if (mapRef.value) {
            mapRef.value.refreshMap();
        }
    }, 350);
};

const toggleSelection = (id: string) => {
    if (selectedVehicles.value.has(id)) selectedVehicles.value.delete(id);
    else selectedVehicles.value.add(id);
};

const handleSubAction = (payload: { action: string, id: string }) => {
    const { action, id } = payload;

    if (action === 'zoom') {
        const v = vehicleStore.vehicles.get(id);
        if (v && mapRef.value) {
            mapRef.value.zoomTo(v.lat, v.lng, isListVisible.value);
        }
    }
    if (action === 'follow') {
        followedVehicleId.value = (followedVehicleId.value === id) ? null : id;
    }
    if (action === 'info') {
        console.log('Details requested for:', id);
    }
};
</script>