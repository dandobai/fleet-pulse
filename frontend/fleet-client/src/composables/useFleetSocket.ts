import { onMounted, onUnmounted } from 'vue';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useVehicleStore } from '@/stores/vehicleStore';

export function useFleetSocket() {
  const store = useVehicleStore();
  let stompClient: Stomp.Client | null = null;

  const connect = () => {
    const socket = new SockJS('http://localhost:8080/ws-fleet');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      console.log('Connected to FleetPulse WebSocket');

      stompClient?.subscribe('/topic/vehicles/', (message) => {
        const wrapper = JSON.parse(message.body);

        if (wrapper.type === 'LIVE_UPDATE') {
          const e = wrapper.data;

          console.log("Adat kibontva:", e);

          store.updateVehicle({
            id: e.vehicleId,
            lat: e.latitude,
            lng: e.longitude,
            timestamp: new Date().toISOString()
          });
        }
      });
    });
  };

  onMounted(() => connect());

  onUnmounted(() => {
    if (stompClient) stompClient.disconnect(() => console.log('Disconnected'));
  });
}