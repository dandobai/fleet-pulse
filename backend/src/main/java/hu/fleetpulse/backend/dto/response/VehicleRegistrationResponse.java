package hu.fleetpulse.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Új jármű regisztrációjának eredménye")
public record VehicleRegistrationResponse(
        @Schema(description = "Létrehozott jármű azonosítója", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id
) {}