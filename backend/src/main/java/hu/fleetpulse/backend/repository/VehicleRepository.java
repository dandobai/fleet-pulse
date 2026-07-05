package hu.fleetpulse.backend.repository;

import hu.fleetpulse.backend.model.entity.Vehicle;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    @Query("SELECT v FROM Vehicle v WHERE function('ST_DWithin', v.position, :point, :radius) = true")
    List<Vehicle> findVehiclesInRadius(@Param("point") Point point, @Param("radius") Double radius);
}