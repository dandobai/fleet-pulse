package hu.fleetpulse.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Jármű korábbi pozíciójának adatai")
public record PositionHistoryDTO(
        @Schema(description = "Rekord egyedi azonosítója", example = "a1b2c3d4-e5f6-7890-abcd-1234567890ab")
        UUID id,
        @Schema(description = "Jármű azonosítója", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID vehicleId,
        @Schema(description = "GPS szélességi fok", example = "47.4979")
        Double latitude,
        @Schema(description = "GPS hosszúsági fok", example = "19.0402")
        Double longitude,
        @Schema(description = "Pozíció rögzítésének ideje", example = "2026-07-05T09:45:00Z")
        LocalDateTime timestamp
) {}