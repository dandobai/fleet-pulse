package hu.fleetpulse.backend.controller;

import hu.fleetpulse.backend.dto.response.ErrorResponse;
import hu.fleetpulse.backend.dto.response.PositionHistoryListResponse;
import hu.fleetpulse.backend.mapper.PositionHistoryMapper;
import hu.fleetpulse.backend.service.PositionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
@Validated
@Tag(name = "Position History", description = "Járművek korábbi pozícióinak lekérdezése")
public class PositionHistoryController {

    private final PositionHistoryService service;
    private final PositionHistoryMapper mapper;

    @GetMapping("/{vehicleId}")
    @Operation(summary = "Jármű útvonalelőzményeinek lekérdezése",
            description = "Egy adott azonosítójú jármű korábbi GPS koordinátáinak listázása időrendben.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sikeres lekérdezés",
                    content = @Content(schema = @Schema(implementation = PositionHistoryListResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Érvénytelen UUID formátum vagy validációs hiba",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "Jármű nem található",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PositionHistoryListResponse> findAllByVehicleId(
            @Parameter(description = "A jármű egyedi UUID azonosítója", required = true)
            @PathVariable @NotNull UUID vehicleId) {

        var positionHistoryDTOs = service.findAllByVehicleId(vehicleId).stream().map(mapper::toDto).toList();
        var response = new PositionHistoryListResponse(positionHistoryDTOs);
        return ResponseEntity.ok(response);
    }
}