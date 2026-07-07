package hu.fleetpulse.backend.listener;

import hu.fleetpulse.backend.event.NotificationCreatedEvent;
import hu.fleetpulse.backend.event.VehicleMovedEvent;
import hu.fleetpulse.backend.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {
    private final WebSocketService webSocketService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotificationCreatedEvent(NotificationCreatedEvent event) {
        log.debug("Broadcasting notification for vehicle: {} via WebSocket", event.vehicleId());

        webSocketService.broadcastNotification(event);

        log.debug("Notification successfully broadcasted for vehicle: {}", event.vehicleId());
    }
}
