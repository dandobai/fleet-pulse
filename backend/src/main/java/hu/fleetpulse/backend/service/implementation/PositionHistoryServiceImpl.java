package hu.fleetpulse.backend.service.implementation;

import hu.fleetpulse.backend.converter.PositionConverter;
import hu.fleetpulse.backend.exception.VehicleNotFoundException;
import hu.fleetpulse.backend.model.entity.PositionHistory;
import hu.fleetpulse.backend.model.value.GeoLocation;
import hu.fleetpulse.backend.repository.PositionHistoryRepository;
import hu.fleetpulse.backend.service.PositionHistoryService;
import hu.fleetpulse.backend.service.VehicleService;
import hu.fleetpulse.backend.validator.GeoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionHistoryServiceImpl implements PositionHistoryService {

    private final PositionHistoryRepository repository;
    private final PositionConverter positionConverter;
    private final VehicleService vehicleService;
    private final GeoValidator geoValidator;

    @Override
    @Transactional
    public void create(UUID vehicleId, Double latitude, Double longitude) {
        Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");

        geoValidator.validate(latitude, longitude);

        validateVehicleExists(vehicleId);

        log.debug("Creating position history for vehicle: {} at coordinates [{}, {}]",
                vehicleId, latitude, longitude);

        PositionHistory history = PositionHistory.builder()
                .vehicleId(vehicleId)
                .position(positionConverter.toPoint(new GeoLocation(latitude, longitude)))
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(history);
        log.info("Successfully recorded position history for vehicle: {}", vehicleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionHistory> findAllByVehicleId(UUID vehicleId) {
        Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");

        validateVehicleExists(vehicleId);

        return repository.findAllByVehicleId(vehicleId);
    }

    private void validateVehicleExists(UUID vehicleId) {
        if (!vehicleService.existsById(vehicleId)) {
            throw new VehicleNotFoundException("Cannot process position history: Vehicle not found with ID: " + vehicleId);
        }
    }

}