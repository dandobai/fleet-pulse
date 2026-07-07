package hu.fleetpulse.backend.service.implementation;

import hu.fleetpulse.backend.dto.websocket.WebSocketPayload;
import hu.fleetpulse.backend.event.NotificationCreatedEvent;
import hu.fleetpulse.backend.event.VehicleMovedEvent;
import hu.fleetpulse.backend.service.WebSocketService;
import hu.fleetpulse.backend.validator.GeoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final GeoValidator geoValidator;

    @Override
    public void broadcastMovement(VehicleMovedEvent event) {
        Objects.requireNonNull(event.vehicleId(), "Vehicle ID cannot be null");
        geoValidator.validate(event.latitude(), event.longitude());

        var liveMessage = new WebSocketPayload("VEHICLE_UPDATE", event);

        messagingTemplate.convertAndSend("/topic/vehicles/", liveMessage);
    }

    @Override
    public void broadcastNotification(NotificationCreatedEvent event) {
        Objects.requireNonNull(event.vehicleId(), "Vehicle ID cannot be null");

        var notificationMessage = new WebSocketPayload("NOTIFICATION_UPDATE", event);

        messagingTemplate.convertAndSend("/topic/notification/", notificationMessage);
    }
}