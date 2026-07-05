import { defineStore } from 'pinia';
import { ref } from 'vue';

export interface VehicleData {
  id: string;
  lat: number;
  lng: number;
  timestamp: string;
}

export const useVehicleStore = defineStore('vehicle', () => {
  const vehicles = ref<Map<string, VehicleData>>(new Map());

  function updateVehicle(data: VehicleData) {
    vehicles.value.set(data.id, data);
  }

  return { vehicles, updateVehicle };
});