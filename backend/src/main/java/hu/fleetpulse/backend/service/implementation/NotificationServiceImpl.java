package hu.fleetpulse.backend.service.implementation;

import hu.fleetpulse.backend.dto.request.CreateNotificationRequest;
import hu.fleetpulse.backend.exception.VehicleNotFoundException;
import hu.fleetpulse.backend.model.entity.Notification;
import hu.fleetpulse.backend.repository.NotificationRepository;
import hu.fleetpulse.backend.service.NotificationService;
import hu.fleetpulse.backend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final VehicleService vehicleService;

    @Override
    @Transactional
    public void create(CreateNotificationRequest request) {
        Objects.requireNonNull(request, "Request cannot be null");
        Objects.requireNonNull(request.vehicleId(), "Vehicle ID cannot be null");
        validateVehicleExists(request.vehicleId());

        log.debug("Creating new notification for vehicle: {}", request.vehicleId());

        Notification note = Notification.builder()
                .vehicleId(request.vehicleId())
                .message(request.message())
                .build();

        repository.save(note);
        log.info("Notification successfully saved for vehicle: {}", request.vehicleId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> findAllByVehicleId(UUID vehicleId) {
        Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");
        validateVehicleExists(vehicleId);

        log.debug("Fetching all notifications for vehicle: {}", vehicleId);

        List<Notification> notifications = repository.findAllByVehicleId(vehicleId);

        log.info("Found {} notifications for vehicle: {}", notifications.size(), vehicleId);
        return notifications;
    }

    private void validateVehicleExists(UUID vehicleId) {
        if (!vehicleService.existsById(vehicleId)) {
            throw new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId);
        }
    }
}