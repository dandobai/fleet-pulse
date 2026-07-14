import { vi, describe, it, expect, beforeEach } from 'vitest';
import { FleetSocketService } from '../../services/fleetSocketService';

vi.mock('@stomp/stompjs', () => {
  const instance = {
    activate: vi.fn(),
    deactivate: vi.fn(),
    subscribe: vi.fn(),
    active: false,
  };

  return {
    Client: class {
      constructor() {
        return instance;
      }
    }
  };
});

import { Client } from '@stomp/stompjs';

describe('FleetSocketService', () => {
  let service: FleetSocketService;

  beforeEach(() => {
    vi.clearAllMocks();
    service = new FleetSocketService();
  });

  it('should activate the STOMP client when connect is called', () => {
    service.connect();
    expect(new Client().activate).toHaveBeenCalled();
  });

  it('should subscribe to a topic and parse the message body correctly', () => {
    const callback = vi.fn();
    const mockData = { test: 'data' };
    const rawMessage = { body: JSON.stringify(mockData) };
    
    (new Client().subscribe as any).mockImplementation((_topic: string, cb: any) => {
      cb(rawMessage);
    });

    service.subscribeToTopic('/topic/test', callback);

    expect(new Client().subscribe).toHaveBeenCalledWith('/topic/test', expect.any(Function));
    expect(callback).toHaveBeenCalledWith(mockData);
  });
});