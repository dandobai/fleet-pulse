package hu.fleetpulse.backend.service;

import hu.fleetpulse.backend.event.VehicleMovedEvent;
import hu.fleetpulse.backend.model.entity.PositionHistory;

import java.util.List;
import java.util.UUID;

public interface PositionHistoryService {
    void create(UUID vehicleId, Double latitude, Double longitude);
    List<PositionHistory> findByVehicleId(UUID vehicleId);
}
