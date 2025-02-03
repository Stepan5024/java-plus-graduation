package ru.practicum.location.mapper;

import org.mapstruct.Mapper;
import ru.practicum.core.api.dto.location.LocationDto;
import ru.practicum.location.model.Location;


@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    LocationDto locationToLocationDto(Location location);

    Location locationDtoToLocation(LocationDto locationDto);
}