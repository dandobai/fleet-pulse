import { describe, it, expect, vi, beforeEach } from 'vitest';
import { MapMarkerManager } from '../../services/mapMarkerManager';
import L from 'leaflet';

vi.mock('leaflet', () => {
  const mockMarker = {
    addTo: vi.fn().mockReturnThis(),
    setLatLng: vi.fn(),
  };

  return {
    default: {
      marker: vi.fn(() => mockMarker),
      divIcon: vi.fn(),
      Map: class {
        removeLayer = vi.fn();
      },
    },
    marker: vi.fn(() => mockMarker),
    Map: class {
      removeLayer = vi.fn();
    },
    divIcon: vi.fn(),
  };
});

describe('MapMarkerManager', () => {
  let manager: MapMarkerManager;
  let mockMapInstance: any;

  beforeEach(() => {
    vi.clearAllMocks();
    mockMapInstance = new (L as any).Map();
    manager = new MapMarkerManager(mockMapInstance);
  });

  it('should add a new marker if it does not exist', () => {
    manager.updateMarker({ id: 'v1', lat: 47, lng: 19 } as any);
    expect((L as any).marker).toHaveBeenCalledWith([47, 19], expect.any(Object));
  });

  it('should update existing marker position instead of creating a new one', () => {
    manager.updateMarker({ id: 'v1', lat: 47, lng: 19 } as any);
    manager.updateMarker({ id: 'v1', lat: 48, lng: 20 } as any);
    
    expect((L as any).marker).toHaveBeenCalledTimes(1);
  });

  it('should remove the marker layer when calling removeMarker', () => {
    manager.updateMarker({ id: 'v1', lat: 47, lng: 19 } as any);
    manager.removeMarker('v1');
    
    expect(mockMapInstance.removeLayer).toHaveBeenCalled();
  });

  it('should sync markers by removing old ones and adding new ones', () => {
    manager.updateMarker({ id: 'v1', lat: 47, lng: 19 } as any);
    const newVehicles = new Map([['v2', { id: 'v2', lat: 50, lng: 20 }]]);

    manager.syncMarkers(newVehicles as any);

    expect(mockMapInstance.removeLayer).toHaveBeenCalled();
    expect((L as any).marker).toHaveBeenCalledWith([50, 20], expect.any(Object));
  });

});