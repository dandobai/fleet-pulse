package hu.fleetpulse.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Jármű alapadatai")
public record VehicleDTO(
        @Schema(description = "Jármű egyedi azonosítója", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @Schema(description = "Jármű jelenlegi szélességi foka", example = "47.4979")
        Double latitude,
        @Schema(description = "Jármű jelenlegi hosszúsági foka", example = "19.0402")
        Double longitude
) {}