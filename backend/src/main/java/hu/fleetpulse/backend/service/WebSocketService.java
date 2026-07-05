package hu.fleetpulse.backend.service;

import hu.fleetpulse.backend.event.VehicleMovedEvent;

public interface WebSocketService {
    void broadcastMovement(VehicleMovedEvent event);
}