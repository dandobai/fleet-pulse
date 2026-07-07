package hu.fleetpulse.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Járműhöz tartozó pozíció-előzmények gyűjteménye")
public record PositionHistoryListResponse(
        @Schema(description = "A lekérdezett pozíció-előzmények listája")
        List<PositionHistoryDTO> history
) {}