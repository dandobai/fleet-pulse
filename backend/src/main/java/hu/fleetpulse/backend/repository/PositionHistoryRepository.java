package hu.fleetpulse.backend.repository;

import hu.fleetpulse.backend.model.entity.PositionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PositionHistoryRepository extends JpaRepository<PositionHistory, UUID> {
    List<PositionHistory> findByVehicleId(@Param("vehicle_id") UUID vehicleId);}