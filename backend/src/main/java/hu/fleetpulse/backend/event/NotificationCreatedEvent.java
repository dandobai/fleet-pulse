package hu.fleetpulse.backend.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationCreatedEvent (UUID vehicleId, String message, String timestamp){
}
