package hu.fleetpulse.backend.unit.mapper;

import hu.fleetpulse.backend.dto.response.NotificationDTO;
import hu.fleetpulse.backend.mapper.NotificationMapper;
import hu.fleetpulse.backend.model.entity.Notification;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationMapperTest {

    private final NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);

    @Test
    void shouldMapToDto() {
        Notification entity = Notification.builder()
                .vehicleId(UUID.randomUUID())
                .message("text")
                .build();

        NotificationDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getMessage(), dto.message());
    }
}