package hu.fleetpulse.backend.unit.mapper;

import hu.fleetpulse.backend.dto.response.NotificationDTO;
import hu.fleetpulse.backend.mapper.NotificationMapper;
import hu.fleetpulse.backend.model.entity.Notification;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    private final NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        Notification entity = createBaseNotificationBuilder()
                .message("Test message")
                .build();

        NotificationDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getVehicleId(), dto.vehicleId());
        assertEquals(entity.getMessage(), dto.message());
    }

    @Test
    void toDto_WhenEntityIsNull_ReturnsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void toDto_WhenFieldsAreNull_MapsCorrectly() {
        Notification entity = Notification.builder()
                .id(UUID.randomUUID())
                .vehicleId(UUID.randomUUID())
                .message(null)
                .build();

        NotificationDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertNull(dto.message());
        assertEquals(entity.getId(), dto.id());
    }

    private Notification.NotificationBuilder createBaseNotificationBuilder() {
        return Notification.builder()
                .id(UUID.randomUUID())
                .vehicleId(UUID.randomUUID());
    }
}