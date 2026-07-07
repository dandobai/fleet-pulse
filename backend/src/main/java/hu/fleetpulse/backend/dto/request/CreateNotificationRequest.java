package hu.fleetpulse.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Új értesítés létrehozására szolgáló kérés adatai")
public record CreateNotificationRequest(
        @Schema(description = "A jármű egyedi azonosítója", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Vehicle ID is required")
        @JsonProperty("vehicle_id")
        UUID vehicleId,

        @Schema(description = "Az értesítés szövege", example = "Sebességtúllépés észlelve!", maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Message cannot be empty")
        @Size(max = 255)
        String message
){}