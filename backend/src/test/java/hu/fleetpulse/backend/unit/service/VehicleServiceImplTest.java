package hu.fleetpulse.backend.unit.service;

import hu.fleetpulse.backend.converter.PositionConverter;
import hu.fleetpulse.backend.dto.request.FindVehiclesInRadiusRequest;
import hu.fleetpulse.backend.dto.request.PositionUpdateRequest;
import hu.fleetpulse.backend.event.VehicleMovedEvent;
import hu.fleetpulse.backend.exception.VehicleNotFoundException;
import hu.fleetpulse.backend.model.entity.Vehicle;
import hu.fleetpulse.backend.model.value.GeoLocation;
import hu.fleetpulse.backend.repository.VehicleRepository;
import hu.fleetpulse.backend.service.PositionHistoryService;
import hu.fleetpulse.backend.service.implementation.VehicleServiceImpl;
import hu.fleetpulse.backend.validator.GeoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private PositionHistoryService positionHistoryService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PositionConverter positionConverter;

    @Mock
    private GeoValidator geoValidator;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void create_ShouldSaveAndReturnId() {
        Vehicle vehicle = Vehicle.builder().id(UUID.randomUUID()).build();
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        assertEquals(vehicle.getId(), vehicleService.create());
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void updatePosition_WhenIdIsNull_ThrowsException() {
        assertThrows(NullPointerException.class, () -> vehicleService.updatePosition(null, new PositionUpdateRequest(47.0, 19.0)));
    }

    @Test
    void updatePosition_WhenRequestIsNull_ThrowsException() {
        assertThrows(NullPointerException.class, () -> vehicleService.updatePosition(UUID.randomUUID(), null));
    }

    @Test
    void updatePosition_WhenVehicleNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VehicleNotFoundException.class, () -> vehicleService.updatePosition(id, new PositionUpdateRequest(47.0, 19.0)));
    }

    @Test
    void updatePosition_Valid_UpdatesAndPublishesEvent() {
        UUID id = UUID.randomUUID();
        PositionUpdateRequest request = new PositionUpdateRequest(47.0, 19.0);
        Vehicle vehicle = Vehicle.builder().id(id).build();
        Point point = mock(Point.class);

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(positionConverter.toPoint(any(GeoLocation.class))).thenReturn(point);

        vehicleService.updatePosition(id, request);

        verify(geoValidator).validate(47.0, 19.0);
        verify(vehicleRepository).save(vehicle);
        verify(eventPublisher).publishEvent(any(VehicleMovedEvent.class));
    }

    @Test
    void findAllByRadius_NullRequest_ThrowsException() {
        assertThrows(NullPointerException.class, () -> vehicleService.findAllByRadius(null));
    }

    @Test
    void findAllByRadius_NegativeRadius_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> vehicleService.findAllByRadius(new FindVehiclesInRadiusRequest(47.0, 19.0, -1.0)));
    }

    @Test
    void findAllByRadius_InvalidCoords_ThrowsException() {
        FindVehiclesInRadiusRequest req = new FindVehiclesInRadiusRequest(99.0, 200.0, 10.0);
        doThrow(new IllegalArgumentException()).when(geoValidator).validate(99.0, 200.0);
        assertThrows(IllegalArgumentException.class, () -> vehicleService.findAllByRadius(req));
    }

    @Test
    void findAllByRadius_Valid_ReturnsList() {
        FindVehiclesInRadiusRequest req = new FindVehiclesInRadiusRequest(47.0, 19.0, 10.0);
        when(positionConverter.toPoint(any(GeoLocation.class))).thenReturn(mock(Point.class));
        when(vehicleRepository.findVehiclesInRadius(any(), eq(10.0))).thenReturn(List.of(Vehicle.builder().build()));
        assertEquals(1, vehicleService.findAllByRadius(req).size());
    }

    @Test
    void findAll_WhenEmpty_ReturnsEmptyList() {
        when(vehicleRepository.findAll()).thenReturn(List.of());
        assertTrue(vehicleService.findAll().isEmpty());
    }

    @Test
    void findAll_WhenPopulated_ReturnsList() {
        when(vehicleRepository.findAll()).thenReturn(List.of(Vehicle.builder().build()));
        assertEquals(1, vehicleService.findAll().size());
    }

    @Test
    void findAll_VerifyRepositoryCall() {
        vehicleService.findAll();
        verify(vehicleRepository).findAll();
    }

    @Test
    void existsById_WhenIdIsNull_ThrowsException() {
        assertThrows(NullPointerException.class, () -> vehicleService.existsById(null));
    }

    @Test
    void existsById_WhenExists_ReturnsTrue() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.existsById(id)).thenReturn(true);
        assertTrue(vehicleService.existsById(id));
    }

    @Test
    void existsById_WhenNotExists_ReturnsFalse() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.existsById(id)).thenReturn(false);
        assertFalse(vehicleService.existsById(id));
    }
}