package hu.fleetpulse.backend.repository;

import hu.fleetpulse.backend.model.entity.PositionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PositionHistoryRepository extends JpaRepository<PositionHistory, UUID> {
    List<PositionHistory> findAllByVehicleId(UUID vehicleId);
}