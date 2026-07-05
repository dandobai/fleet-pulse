package hu.fleetpulse.backend.service;

import hu.fleetpulse.backend.dto.request.CreateNotificationRequest;
import hu.fleetpulse.backend.model.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void create(CreateNotificationRequest request);
    List<Notification> findAllByVehicleId(UUID vehicleId);
}