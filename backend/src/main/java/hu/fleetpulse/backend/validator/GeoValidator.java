package hu.fleetpulse.backend.validator;

import org.springframework.stereotype.Component;

@Component
public class GeoValidator {
    public void validate(Double lat, Double lon) {
        if (lat == null || lon == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
}
