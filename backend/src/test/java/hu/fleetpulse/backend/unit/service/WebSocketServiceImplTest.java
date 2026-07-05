package hu.fleetpulse.backend.unit.service;

import hu.fleetpulse.backend.dto.websocket.WebSocketPayload;
import hu.fleetpulse.backend.event.VehicleMovedEvent;
import hu.fleetpulse.backend.service.implementation.WebSocketServiceImpl;
import hu.fleetpulse.backend.validator.GeoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketServiceImplTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private GeoValidator geoValidator;

    @InjectMocks
    private WebSocketServiceImpl webSocketService;

    @Test
    void broadcastMovement_WhenVehicleIdIsNull_ThenThrowNullPointerException() {
        VehicleMovedEvent event = new VehicleMovedEvent(null, 47.0, 19.0);

        assertThrows(NullPointerException.class, () ->
                webSocketService.broadcastMovement(event));

        verifyNoInteractions(geoValidator, messagingTemplate);
    }

    @Test
    void broadcastMovement_WhenGeoValidationFails_ThenThrowException() {
        UUID id = UUID.randomUUID();
        VehicleMovedEvent event = new VehicleMovedEvent(id, 99.0, 200.0);

        doThrow(new IllegalArgumentException("Invalid coordinates"))
                .when(geoValidator).validate(99.0, 200.0);

        assertThrows(IllegalArgumentException.class, () ->
                webSocketService.broadcastMovement(event));

        verify(messagingTemplate, never()).convertAndSend(anyString(), any(WebSocketPayload.class));
    }

    @Test
    void broadcastMovement_WhenValidEvent_ThenBroadcastSuccessfully() {
        UUID id = UUID.randomUUID();
        VehicleMovedEvent event = new VehicleMovedEvent(id, 47.0, 19.0);

        webSocketService.broadcastMovement(event);

        verify(geoValidator).validate(47.0, 19.0);
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/vehicles/"), any(WebSocketPayload.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/history/" + id), any(WebSocketPayload.class));
    }
}