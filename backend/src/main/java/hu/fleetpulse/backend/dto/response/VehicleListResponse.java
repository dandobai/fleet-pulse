package hu.fleetpulse.backend.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Regisztrált járművek listája")
public record VehicleListResponse(
        @Schema(description = "A flotta járművei")
        List<VehicleDTO> vehicles
) {}