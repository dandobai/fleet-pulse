package hu.fleetpulse.backend.controller;

import hu.fleetpulse.backend.dto.request.FindVehiclesInRadiusRequest;
import hu.fleetpulse.backend.dto.request.PositionUpdateRequest;
import hu.fleetpulse.backend.dto.response.VehicleListResponse;
import hu.fleetpulse.backend.dto.response.VehicleRegistrationResponse;
import hu.fleetpulse.backend.mapper.VehicleMapper;
import hu.fleetpulse.backend.service.VehicleService;
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
@RequiredArgsConstructor
@Validated
@Tag(name = "Vehicle Management", description = "Járművek életciklusának kezelése: regisztráció, pozíciókövetés és flotta lekérdezések.")
public class VehicleController {

    private final VehicleService service;
    private final VehicleMapper mapper;

    @PostMapping("/vehicles")
    @Operation(summary = "Új jármű regisztrálása", description = "Létrehoz egy új jármű rekordot a rendszerben és visszaadja a hozzá tartozó egyedi azonosítót.")
    @ApiResponse(responseCode = "200", description = "Sikeres regisztráció", content = @Content(schema = @Schema(implementation = VehicleRegistrationResponse.class)))
    public ResponseEntity<VehicleRegistrationResponse> create() {
        var response = new VehicleRegistrationResponse(service.create());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/vehicle/{id}")
    @Operation(summary = "Jármű pozíciójának frissítése", description = "Egy adott azonosítójú jármű aktuális földrajzi koordinátáinak frissítése.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pozíció sikeresen frissítve"),
            @ApiResponse(responseCode = "404", description = "Jármű nem található")
    })
    public ResponseEntity<Void> updatePosition(
            @Parameter(description = "A jármű egyedi UUID azonosítója", required = true) @PathVariable UUID id,
            @Valid @RequestBody PositionUpdateRequest request) {
        service.updatePosition(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vehicles")
    @Operation(summary = "Járművek keresése körzeten belül", description = "Lekérdezi azokat a járműveket, amelyek egy megadott GPS koordináta és sugár (km) által meghatározott területen belül találhatók.")
    @ApiResponse(responseCode = "200", description = "Sikeres találati lista", content = @Content(schema = @Schema(implementation = VehicleListResponse.class)))
    public ResponseEntity<VehicleListResponse> findAllByRadius(@Valid FindVehiclesInRadiusRequest request) {
        var response = new VehicleListResponse(service.findAllByRadius(request).stream().map(mapper::toDto).toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Összes jármű lekérdezése", description = "Visszaadja a rendszerben regisztrált összes jármű listáját, függetlenül a pozíciójuktól.")
    @ApiResponse(responseCode = "200", description = "Összes jármű listája", content = @Content(schema = @Schema(implementation = VehicleListResponse.class)))
    public ResponseEntity<VehicleListResponse> findAll() {
        var response = new VehicleListResponse(service.findAll().stream().map(mapper::toDto).toList());
        return ResponseEntity.ok(response);
    }
}