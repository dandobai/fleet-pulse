package hu.fleetpulse.backend.listener;

import hu.fleetpulse.backend.event.VehicleMovedEvent;
import hu.fleetpulse.backend.service.PositionHistoryService;
import hu.fleetpulse.backend.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleEventListener {

    private final PositionHistoryService positionHistoryService;
    private final WebSocketService webSocketService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleVehicleMovedEvent(VehicleMovedEvent event) {
        log.debug("Processing movement for vehicle: {}", event.vehicleId());

        positionHistoryService.create(event.vehicleId(),event.latitude(),event.longitude());

        webSocketService.broadcastMovement(event);

        log.info("Successfully processed movement for vehicle: {}", event.vehicleId());
    }
}