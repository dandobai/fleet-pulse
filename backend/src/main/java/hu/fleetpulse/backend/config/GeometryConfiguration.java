package hu.fleetpulse.backend.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeometryConfiguration {

    private static final int WGS84_SRID = 4326;

    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), WGS84_SRID);
    }
}