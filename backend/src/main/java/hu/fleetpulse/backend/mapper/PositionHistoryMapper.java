package hu.fleetpulse.backend.mapper;

import hu.fleetpulse.backend.dto.response.PositionHistoryDTO;
import hu.fleetpulse.backend.model.entity.PositionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PositionHistoryMapper {
    @Mapping(target = "latitude", source = "position.y")
    @Mapping(target = "longitude", source = "position.x")
    PositionHistoryDTO toDto(PositionHistory entity);
}