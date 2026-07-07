package hu.fleetpulse.backend.mapper;

import hu.fleetpulse.backend.dto.response.NotificationDTO;
import hu.fleetpulse.backend.model.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDTO toDto(Notification notification);
}