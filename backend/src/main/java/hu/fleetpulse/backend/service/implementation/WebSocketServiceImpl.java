package hu.fleetpulse.backend.service.implementation;

import hu.fleetpulse.backend.dto.websocket.WebSocketPayload;
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
        geoValidator.validate(event.latitude(),event.longitude());

        var liveMessage = new WebSocketPayload("LIVE_UPDATE", event);
        messagingTemplate.convertAndSend("/topic/vehicles/", liveMessage);

        var historyMessage = new WebSocketPayload("HISTORY_UPDATE", event);
        messagingTemplate.convertAndSend("/topic/history/" + event.vehicleId(), historyMessage);
    }
}