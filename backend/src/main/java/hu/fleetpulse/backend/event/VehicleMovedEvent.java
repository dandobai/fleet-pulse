package hu.fleetpulse.backend.event;

import java.util.UUID;

public record VehicleMovedEvent(UUID vehicleId, Double latitude, Double longitude) {}