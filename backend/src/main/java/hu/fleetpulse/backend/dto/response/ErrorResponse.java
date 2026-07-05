package hu.fleetpulse.backend.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        Map<String, String> details // Ide kerülhetnek a mező-specifikus hibák
) {}
