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
    private final GeoValidator geoValidator;

    @Override
    @Transactional
    public void create(UUID vehicleId, Double latitude, Double longitude) {
        Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");

        geoValidator.validate(latitude, longitude);

        log.debug("Creating position history for vehicle: {} at coordinates [{}, {}]",
                vehicleId, latitude, longitude);

        PositionHistory history = new PositionHistory();
        history.setVehicleId(vehicleId);
        history.setPosition(positionConverter.toPoint(new GeoLocation(latitude, longitude)));
        history.setTimestamp(LocalDateTime.now());

        repository.save(history);
        log.info("Successfully recorded position history for vehicle: {}", vehicleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionHistory> findByVehicleId(UUID vehicleId) {
        Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");
        return repository.findByVehicleId(vehicleId);
    }
}