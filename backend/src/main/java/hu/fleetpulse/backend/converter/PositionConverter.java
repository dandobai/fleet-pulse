package hu.fleetpulse.backend.converter;

import hu.fleetpulse.backend.model.value.GeoLocation;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionConverter {

    private final GeometryFactory geometryFactory;

    public Point toPoint(GeoLocation location) {
        return geometryFactory.createPoint(new Coordinate(location.longitude(), location.latitude()));
    }
}