package hu.fleetpulse.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Jármű pozíciójának frissítésére szolgáló adatok")
public record PositionUpdateRequest(
        @Schema(description = "Új GPS szélességi fok", example = "47.4979", minimum = "-90.0", maximum = "90.0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Latitude cannot be null")
        @DecimalMin("-90.0") @DecimalMax("90.0")
        Double latitude,

        @Schema(description = "Új GPS hosszúsági fok", example = "19.0402", minimum = "-180.0", maximum = "180.0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Longitude cannot be null")
        @DecimalMin("-180.0") @DecimalMax("180.0")
        Double longitude
) {}