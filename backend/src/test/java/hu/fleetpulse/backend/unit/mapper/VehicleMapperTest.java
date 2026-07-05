package hu.fleetpulse.backend.unit.mapper;

import hu.fleetpulse.backend.dto.response.VehicleDTO;
import hu.fleetpulse.backend.mapper.VehicleMapper;
import hu.fleetpulse.backend.model.entity.Vehicle;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleMapperTest {

    private final VehicleMapper mapper = Mappers.getMapper(VehicleMapper.class);
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Test
    void shouldMapToDto() {
        UUID id = UUID.randomUUID();
        double lat = 47.4979;
        double lon = 19.0402;

        Point position = geometryFactory.createPoint(new Coordinate(lon, lat));

        Vehicle vehicle = Vehicle.builder()
                .id(id)
                .position(position)
                .build();

        VehicleDTO dto = mapper.toDto(vehicle);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(lat, dto.latitude(), 0.0001, "A szélességi foknak egyeznie kell");
        assertEquals(lon, dto.longitude(), 0.0001, "A hosszúsági foknak egyeznie kell");
    }

    @Test
    void shouldHandleNullPosition() {
        Vehicle vehicle = Vehicle.builder()
                .id(UUID.randomUUID())
                .position(null)
                .build();

        VehicleDTO dto = mapper.toDto(vehicle);

        assertNotNull(dto);
        assertNull(dto.latitude());
        assertNull(dto.longitude());
    }
}