package hu.fleetpulse.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Járműhöz tartozó értesítések gyűjteménye")
public record NotificationListResponse(
        @Schema(description = "Az értesítések listája")
        List<NotificationDTO> notifications
) {}