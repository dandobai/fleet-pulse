package hu.fleetpulse.backend.unit.validator;

import hu.fleetpulse.backend.validator.GeoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GeoValidatorTest {

    private GeoValidator geoValidator;

    @BeforeEach
    void setUp() {
        geoValidator = new GeoValidator();
    }

    @Test
    void validate_NullCoordinates_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> geoValidator.validate(null, 19.0));
        assertThrows(IllegalArgumentException.class, () -> geoValidator.validate(47.0, null));
    }

    @ParameterizedTest
    @CsvSource({
            "-90.1, 0.0",  // Latitude too low
            "90.1, 0.0",   // Latitude too high
            "0.0, -180.1", // Longitude too low
            "0.0, 180.1"   // Longitude too high
    })
    void validate_InvalidCoordinates_ThrowsException(Double lat, Double lon) {
        assertThrows(IllegalArgumentException.class, () -> geoValidator.validate(lat, lon));
    }

    @ParameterizedTest
    @CsvSource({
            "-90.0, -180.0", // Boundary minimum
            "90.0, 180.0",   // Boundary maximum
            "47.4979, 19.0402" // Typical valid coordinates
    })
    void validate_ValidCoordinates_DoesNotThrowException(Double lat, Double lon) {
        assertDoesNotThrow(() -> geoValidator.validate(lat, lon));
    }
}