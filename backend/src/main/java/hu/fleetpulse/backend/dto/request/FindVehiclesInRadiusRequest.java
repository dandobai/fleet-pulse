package hu.fleetpulse.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Keresési paraméterek megadott körzeten belüli járművekhez")
public record FindVehiclesInRadiusRequest(
        @Schema(description = "GPS szélességi fok", example = "47.4979", minimum = "-90.0", maximum = "90.0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Latitude is required")
        @DecimalMin("-90.0") @DecimalMax("90.0")
        Double latitude,

        @Schema(description = "GPS hosszúsági fok", example = "19.0402", minimum = "-180.0", maximum = "180.0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Longitude is required")
        @DecimalMin("-180.0") @DecimalMax("180.0")
        Double longitude,

        @Schema(description = "Keresési sugár kilométerben", example = "5.0", minimum = "0.0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Radius is required")
        @Positive(message = "Radius must be positive")
        Double radius
){}