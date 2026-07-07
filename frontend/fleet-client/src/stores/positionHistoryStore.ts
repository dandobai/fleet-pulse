import { defineStore } from 'pinia';
import { ref } from 'vue';

export interface PositionData {
  lat: number;
  lng: number;
  timestamp: string;
}

export const usePositionHistoryStore = defineStore('positionHistory', () => {
  const histories = ref<Map<string, PositionData[]>>(new Map());
  const loading = ref<Map<string, boolean>>(new Map());

  function updateMap(callback: (map: Map<string, PositionData[]>) => void) {
    const newHistories = new Map(histories.value);
    callback(newHistories);
    histories.value = newHistories;
  }

  function updateLoading(vehicleId: string, isLoading: boolean) {
    const newLoading = new Map(loading.value);
    if (isLoading) {
      newLoading.set(vehicleId, true);
    } else {
      newLoading.delete(vehicleId);
    }
    loading.value = newLoading;
  }

  function isHistoryActive(vehicleId: string): boolean {
    return histories.value.has(vehicleId);
  }

  function getHistories(vehicleId: string): PositionData[] {
    return histories.value.get(vehicleId) || [];
  }

  function appendPosition(vehicleId: string, pos: PositionData) {
    if (isHistoryActive(vehicleId)) {
      updateMap((map) => {
        const currentList = map.get(vehicleId) || [];
        map.set(vehicleId, [...currentList, pos]);
      });
    }
  }

  function setHistory(vehicleId: string, positions: PositionData[]) {
    updateMap((map) => {
      map.set(vehicleId, positions);
    });
  }

  function deletePositionHistory(vehicleId: string) {
    updateMap((map) => {
      map.delete(vehicleId);
    });
    updateLoading(vehicleId, false);
  }

  function toggleVehicle(vehicleId: string) {
    if (isHistoryActive(vehicleId)) {
      deletePositionHistory(vehicleId);
    } else {
      updateMap((map) => {
        map.set(vehicleId, []);
      });
      updateLoading(vehicleId, true);
    }
  }

  return { 
    histories, 
    loading, 
    isHistoryActive, 
    getHistories, 
    appendPosition, 
    setHistory, 
    deletePositionHistory, 
    toggleVehicle,
    updateLoading
  };
});