package hu.fleetpulse.backend.service;

import hu.fleetpulse.backend.dto.request.FindVehiclesInRadiusRequest;
import hu.fleetpulse.backend.dto.request.PositionUpdateRequest;
import hu.fleetpulse.backend.model.entity.Vehicle;

import java.util.List;
import java.util.UUID;

public interface VehicleService {
    UUID create();
    void updatePosition(UUID id, PositionUpdateRequest request);
    List<Vehicle> findAllByRadius(FindVehiclesInRadiusRequest request);
    List<Vehicle> findAll();
    boolean existsById(UUID id);
}