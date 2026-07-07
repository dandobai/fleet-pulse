package hu.fleetpulse.backend.controller;

import hu.fleetpulse.backend.dto.request.CreateNotificationRequest;
import hu.fleetpulse.backend.dto.response.NotificationListResponse;
import hu.fleetpulse.backend.mapper.NotificationMapper;
import hu.fleetpulse.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Validated
@Tag(name = "Notifications", description = "Értesítések kezelése: új értesítések létrehozása és járműhöz kötött lekérdezések.")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper mapper;

    @PostMapping
    @Operation(summary = "Új értesítés létrehozása", description = "Létrehoz egy új értesítést a rendszerben a megadott adatok alapján.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Értesítés sikeresen létrehozva"), @ApiResponse(responseCode = "400", description = "Érvénytelen kérés (validációs hiba)")})
    public ResponseEntity<Void> create(@Parameter(description = "Az értesítés adatai", required = true) @Valid @RequestBody CreateNotificationRequest request) {
        notificationService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Értesítések lekérdezése jármű szerint", description = "Lekérdezi az összes, egy adott járműhöz tartozó értesítést.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Sikeres lekérdezés", content = @Content(schema = @Schema(implementation = NotificationListResponse.class))), @ApiResponse(responseCode = "404", description = "Jármű nem található")})
    public ResponseEntity<NotificationListResponse> findAllByVehicleId(@Parameter(description = "A jármű egyedi UUID azonosítója", required = true) @PathVariable UUID vehicleId) {
        var notificationDTOs = notificationService.findAllByVehicleId(vehicleId).stream().map(mapper::toDto).toList();
        var response = new NotificationListResponse(notificationDTOs);
        return ResponseEntity.ok(response);
    }
}