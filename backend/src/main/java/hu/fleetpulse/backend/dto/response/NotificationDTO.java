package hu.fleetpulse.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Egyedi értesítés adatai")
public record NotificationDTO(
        @Schema(description = "Értesítés egyedi azonosítója", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @Schema(description = "Érintett jármű azonosítója", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID vehicleId,
        @Schema(description = "Értesítés szöveges üzenete", example = "Alacsony akkumulátor töltöttség")
        String message,
        @Schema(description = "Értesítés ideje (ISO-8601)", example = "2026-07-05T10:00:00Z")
        LocalDateTime timestamp
) {}