package hu.fleetpulse.backend.unit.service;

import hu.fleetpulse.backend.dto.request.CreateNotificationRequest;
import hu.fleetpulse.backend.exception.VehicleNotFoundException;
import hu.fleetpulse.backend.model.entity.Notification;
import hu.fleetpulse.backend.repository.NotificationRepository;
import hu.fleetpulse.backend.service.VehicleService;
import hu.fleetpulse.backend.service.implementation.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void create_WhenRequestIsNull_ThenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> notificationService.create(null));
        verifyNoInteractions(repository);
    }

    @Test
    void create_WhenVehicleIdIsNull_ThenThrowNullPointerException() {
        CreateNotificationRequest request = new CreateNotificationRequest(null, "Test msg");
        assertThrows(NullPointerException.class, () -> notificationService.create(request));
        verifyNoInteractions(repository);
    }

    @Test
    void create_WhenVehicleDoesNotExist_ThenThrowVehicleNotFoundException() {
        UUID id = UUID.randomUUID();
        CreateNotificationRequest request = new CreateNotificationRequest(id, "Test msg");
        when(vehicleService.existsById(id)).thenReturn(false);

        assertThrows(VehicleNotFoundException.class, () -> notificationService.create(request));
        verify(repository, never()).save(any(Notification.class));
    }

    @Test
    void create_WhenValidRequest_ThenSaveNotification() {
        UUID id = UUID.randomUUID();
        CreateNotificationRequest request = new CreateNotificationRequest(id, "Test msg");
        when(vehicleService.existsById(id)).thenReturn(true);

        notificationService.create(request);

        verify(repository, times(1)).save(any(Notification.class));
    }

    @Test
    void findAll_WhenVehicleIdIsNull_ThenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> notificationService.findAllByVehicleId(null));
    }

    @Test
    void findAll_WhenVehicleDoesNotExist_ThenThrowVehicleNotFoundException() {
        UUID id = UUID.randomUUID();
        when(vehicleService.existsById(id)).thenReturn(false);

        assertThrows(VehicleNotFoundException.class, () -> notificationService.findAllByVehicleId(id));
    }

    @Test
    void findAll_WhenValidId_ThenReturnNotifications() {
        UUID id = UUID.randomUUID();

        Notification notification = Notification.builder()
                .vehicleId(id)
                .message("Test notification")
                .build();

        when(vehicleService.existsById(id)).thenReturn(true);
        when(repository.findAllByVehicleId(id)).thenReturn(List.of(notification));

        List<Notification> result = notificationService.findAllByVehicleId(id);

        assertEquals(1, result.size());
        assertEquals(id, result.getFirst().getVehicleId());
        verify(repository, times(1)).findAllByVehicleId(id);
    }
}