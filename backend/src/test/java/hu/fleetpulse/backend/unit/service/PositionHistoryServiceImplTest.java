package hu.fleetpulse.backend.unit.service;

import hu.fleetpulse.backend.converter.PositionConverter;
import hu.fleetpulse.backend.exception.VehicleNotFoundException;
import hu.fleetpulse.backend.model.entity.PositionHistory;
import hu.fleetpulse.backend.repository.PositionHistoryRepository;
import hu.fleetpulse.backend.service.VehicleService;
import hu.fleetpulse.backend.service.implementation.PositionHistoryServiceImpl;
import hu.fleetpulse.backend.validator.GeoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionHistoryServiceImplTest {

    @Mock
    private PositionHistoryRepository repository;

    @Mock
    private PositionConverter positionConverter;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private GeoValidator geoValidator;

    @InjectMocks
    private PositionHistoryServiceImpl positionHistoryService;

    @Test
    void create_WhenVehicleIdIsNull_ThenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                positionHistoryService.create(null, 47.0, 19.0));

        verifyNoInteractions(repository);
    }

    @Test
    void create_WhenGeoValidationFails_ThenThrowIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        Double invalidLat = 99.0;
        Double invalidLon = 200.0;

        doThrow(new IllegalArgumentException("Invalid coordinates"))
                .when(geoValidator).validate(invalidLat, invalidLon);

        assertThrows(IllegalArgumentException.class, () ->
                positionHistoryService.create(id, invalidLat, invalidLon)
        );

        verify(repository, never()).save(any());
        verify(vehicleService, never()).existsById(any());
    }

    @Test
    void create_WhenVehicleDoesNotExist_ThenThrowVehicleNotFoundException() {
        UUID id = UUID.randomUUID();
        when(vehicleService.existsById(id)).thenReturn(false);

        assertThrows(VehicleNotFoundException.class, () ->
                positionHistoryService.create(id, 47.0, 19.0));

        verify(repository, never()).save(any());
    }

    @Test
    void create_WhenValidData_ThenSaveHistory() {
        UUID id = UUID.randomUUID();
        when(vehicleService.existsById(id)).thenReturn(true);

        positionHistoryService.create(id, 47.0, 19.0);

        verify(geoValidator, times(1)).validate(47.0, 19.0);
        verify(repository, times(1)).save(any(PositionHistory.class));
    }

    @Test
    void findAllByVehicleId_WhenIdIsNull_ThenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                positionHistoryService.findAllByVehicleId(null));

        verifyNoInteractions(repository);
    }

    @Test
    void findAllByVehicleId_WhenVehicleNotFound_ThenThrowException() {
        UUID id = UUID.randomUUID();
        when(vehicleService.existsById(id)).thenReturn(false);

        assertThrows(VehicleNotFoundException.class, () ->
                positionHistoryService.findAllByVehicleId(id));
    }

    @Test
    void findAllByVehicleId_WhenValidId_ThenReturnList() {
        UUID id = UUID.randomUUID();
        when(vehicleService.existsById(id)).thenReturn(true);
        when(repository.findAllByVehicleId(id)).thenReturn(List.of(new PositionHistory()));

        List<PositionHistory> result = positionHistoryService.findAllByVehicleId(id);

        assertFalse(result.isEmpty());
        verify(repository, times(1)).findAllByVehicleId(id);
    }
}