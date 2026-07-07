package hu.fleetpulse.backend.unit.mapper;

import hu.fleetpulse.backend.converter.PositionConverter;
import hu.fleetpulse.backend.dto.response.PositionHistoryDTO;
import hu.fleetpulse.backend.mapper.PositionHistoryMapper;
import hu.fleetpulse.backend.model.entity.PositionHistory;
import hu.fleetpulse.backend.model.value.GeoLocation;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PositionHistoryMapperTest {

    private final PositionHistoryMapper mapper = Mappers.getMapper(PositionHistoryMapper.class);
    private final PositionConverter converter = new PositionConverter(new GeometryFactory());

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        GeoLocation geoLocation = new GeoLocation(19.0, 47.0);
        PositionHistory entity = createBaseEntityBuilder().position(converter.toPoint(geoLocation)).build();

        PositionHistoryDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getVehicleId(), dto.vehicleId());
        assertEquals(entity.getTimestamp(), dto.timestamp());
        assertEquals(geoLocation.latitude(), dto.latitude());
        assertEquals(geoLocation.longitude(), dto.longitude());
    }

    @Test
    void toDto_WhenEntityIsNull_ReturnsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void toDto_WhenPositionIsNull_SetsCoordinatesToNull() {
        PositionHistory entity = createBaseEntityBuilder().position(null).build();

        PositionHistoryDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertNull(dto.latitude());
        assertNull(dto.longitude());
    }

    private PositionHistory.PositionHistoryBuilder createBaseEntityBuilder() {
        return PositionHistory.builder().id(UUID.randomUUID()).vehicleId(UUID.randomUUID()).timestamp(LocalDateTime.now());
    }
}