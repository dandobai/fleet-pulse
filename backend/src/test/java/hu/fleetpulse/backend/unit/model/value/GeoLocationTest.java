package hu.fleetpulse.backend.unit.model.value;

import hu.fleetpulse.backend.model.value.GeoLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GeoLocationTest {

    @Test
    void shouldCreateGeoLocation_WhenCoordinatesAreValid() {
        assertDoesNotThrow(() -> new GeoLocation(47.0, 19.0));
    }

    @ParameterizedTest
    @CsvSource({
            "-91.0, 0.0",   // Latitude too low
            "91.0, 0.0",    // Latitude too high
            "0.0, -181.0",  // Longitude too low
            "0.0, 181.0"    // Longitude too high
    })
    void shouldThrowException_WhenCoordinatesAreInvalid(double lat, double lon) {
        assertThrows(IllegalArgumentException.class, () -> new GeoLocation(lat, lon));
    }

    @Test
    void shouldAcceptBoundaryValues() {
        assertDoesNotThrow(() -> new GeoLocation(-90.0, -180.0));
        assertDoesNotThrow(() -> new GeoLocation(90.0, 180.0));
    }
}