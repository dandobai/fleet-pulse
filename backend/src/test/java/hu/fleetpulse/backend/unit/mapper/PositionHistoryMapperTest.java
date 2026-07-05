package hu.fleetpulse.backend.unit.mapper;

import hu.fleetpulse.backend.converter.PositionConverter;
import hu.fleetpulse.backend.dto.response.PositionHistoryDTO; // Feltételeztem a DTO nevét
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

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final PositionConverter positionConverter = new PositionConverter(geometryFactory);
    private final PositionHistoryMapper mapper = Mappers.getMapper(PositionHistoryMapper.class);

    @Test
    void shouldMapToDto() {
        // 1. Arrange - Felkészülés az adatokkal
        UUID entityId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        GeoLocation geoLocation = new GeoLocation(19.0, 47.0); // lon, lat
        LocalDateTime now = LocalDateTime.now();

        PositionHistory entity = PositionHistory.builder()
                .id(entityId)
                .vehicleId(vehicleId)
                .position(positionConverter.toPoint(geoLocation))
                .timestamp(now)
                .build();

        // 2. Act - Mapping végrehajtása
        PositionHistoryDTO dto = mapper.toDto(entity);

        // 3. Assert - Minden mező ellenőrzése
        assertNotNull(dto, "A DTO nem lehet null");
        assertEquals(entity.getId(), dto.id(), "Az ID-nak egyeznie kell");
        assertEquals(entity.getVehicleId(), dto.vehicleId(), "A vehicleId-nak egyeznie kell");
        assertEquals(entity.getTimestamp(), dto.timestamp(), "A timestamp-nek egyeznie kell");

        // Ellenőrizzük a koordinátákat is, ha a DTO-ban is GeoLocation van
        assertNotNull(dto.latitude(), "A pozíció nem lehet null");
        assertNotNull(dto.longitude(), "A pozíció nem lehet null");
        assertEquals(geoLocation.latitude(), dto.latitude());
        assertEquals(geoLocation.longitude(), dto.longitude());
    }
}