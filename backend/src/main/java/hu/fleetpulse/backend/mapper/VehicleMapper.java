package hu.fleetpulse.backend.mapper;

import hu.fleetpulse.backend.dto.response.VehicleDTO;
import hu.fleetpulse.backend.model.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    @Mapping(target = "latitude", source = "position.y")
    @Mapping(target = "longitude", source = "position.x")
    VehicleDTO toDto(Vehicle vehicle);
}