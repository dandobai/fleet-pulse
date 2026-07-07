import { defineStore } from 'pinia';
import { ref } from 'vue';
import { usePositionHistoryStore } from './positionHistoryStore';

export interface VehicleData {
  id: string;
  lat: number;
  lng: number;
  timestamp: string;
}

export const useVehicleStore = defineStore('vehicle', () => {
  const vehicles = ref<Map<string, VehicleData>>(new Map());
  const historyStore = usePositionHistoryStore();

  function updateVehicle(data: VehicleData) {
    vehicles.value.set(data.id, data);

    if (historyStore.histories.has(data.id)) {
      historyStore.appendPosition(data.id, {
        lat: data.lat,
        lng: data.lng,
        timestamp: data.timestamp
      });
    }
  }

  return { vehicles, updateVehicle };
});