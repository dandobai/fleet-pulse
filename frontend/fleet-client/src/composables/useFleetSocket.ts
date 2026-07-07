import { onMounted, onUnmounted } from 'vue';
import { fleetSocketService } from '@/services/fleetSocketService';
import { useVehicleStore } from '@/stores/vehicleStore';
import { useNotificationStore } from '@/stores/notificationStore';

export function useFleetSocket() {
  const vehicleStore = useVehicleStore();
  const notificationStore = useNotificationStore();

  const handleVehicleUpdate = (payload: any) => {
    const data = payload.data;
    
    vehicleStore.updateVehicle({
      id: data.vehicleId,
      lat: data.latitude,
      lng: data.longitude,
      timestamp: new Date().toISOString()
    });
  };

  onMounted(() => {
    fleetSocketService.connect();

    const checkConnection = setInterval(() => {
      if (fleetSocketService.isConnected) {
        clearInterval(checkConnection);
        
        fleetSocketService.subscribeToTopic('/topic/vehicles/', handleVehicleUpdate);
        
        fleetSocketService.subscribeToTopic('/topic/notification/', (payload) => {
          notificationStore.addNotification(payload.data);
        });
        
        console.log("WebSocket feliratkozások aktívak.");
      }
    }, 100);
  });

  onUnmounted(() => {
    fleetSocketService.disconnect();
  });
}