package hu.fleetpulse.backend.service.implementation;

import hu.fleetpulse.backend.converter.PositionConverter;
import hu.fleetpulse.backend.dto.request.FindVehiclesInRadiusRequest;
import hu.fleetpulse.backend.dto.request.PositionUpdateRequest;
import hu.fleetpulse.backend.event.VehicleMovedEvent;
import hu.fleetpulse.backend.exception.VehicleNotFoundException;
import hu.fleetpulse.backend.model.entity.Vehicle;
import hu.fleetpulse.backend.model.value.GeoLocation;
import hu.fleetpulse.backend.repository.VehicleRepository;
import hu.fleetpulse.backend.service.VehicleService;
import hu.fleetpulse.backend.validator.GeoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PositionConverter positionConverter;
    private final GeoValidator geoValidator;

    @Override
    @Transactional
    public UUID create() {
        Vehicle vehicle = vehicleRepository.save(new Vehicle());
        log.info("Vehicle registered successfully with ID: {}", vehicle.getId());
        return vehicle.getId();
    }

    @Override
    @Transactional
    public void updatePosition(UUID id, PositionUpdateRequest request) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(request, "Request cannot be null");
        geoValidator.validate(request.latitude(), request.longitude());

        Vehicle vehicle = findVehicleOrThrow(id);

        Point newPosition = positionConverter.toPoint(new GeoLocation(request.latitude(), request.longitude()));
        vehicle.setPosition(newPosition);

        vehicleRepository.save(vehicle);
        log.info("Position updated for vehicle {}: [{}, {}]", id, request.latitude(), request.longitude());
        eventPublisher.publishEvent(new VehicleMovedEvent(id, request.latitude(), request.longitude()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAllByRadius(FindVehiclesInRadiusRequest request) {
        Objects.requireNonNull(request, "Request cannot be null");
        geoValidator.validate(request.latitude(), request.longitude());

        if (request.radius() < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }

        Point searchPoint = positionConverter.toPoint(new GeoLocation(request.latitude(), request.longitude()));
        return vehicleRepository.findVehiclesInRadius(searchPoint, request.radius());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID vehicleId) {
        Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");

        return vehicleRepository.existsById(vehicleId);
    }

    private Vehicle findVehicleOrThrow(UUID vehicleId) {
        Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId));
    }
}