import { Client } from '@stomp/stompjs';

export class FleetSocketService {
  private client: Client;
  public isConnected = false;
  constructor() {
    this.client = new Client({
      brokerURL: __WS_URL__,
      onConnect: () => {
        this.isConnected = true;
        console.log('STOMP connected');
      },
      onDisconnect: () => {
        this.isConnected = false;
      }
    });
  }

  connect() {
    if (!this.client.active) {
      this.client.activate();
    }
  }

  subscribeToTopic(topic: string, callback: (data: any) => void) {
    this.client.subscribe(topic, (message) => {
      callback(JSON.parse(message.body));
    });
  }

  disconnect() {
    this.client.deactivate();
  }
}

export const fleetSocketService = new FleetSocketService();