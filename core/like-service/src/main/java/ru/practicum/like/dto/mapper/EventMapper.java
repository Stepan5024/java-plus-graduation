package ru.practicum.like.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.practicum.core.api.dto.category.CategoryDto;
import ru.practicum.core.api.dto.event.EventFullDto;
import ru.practicum.core.api.dto.location.LocationDto;
import ru.practicum.core.api.dto.user.UserShortDto;
import ru.practicum.core.api.enums.EventState;
import ru.practicum.like.dto.EventDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "state", target = "state"),
            @Mapping(source = "participantLimit", target = "participantLimit"),
            @Mapping(source = "requestModeration", target = "requestModeration"),
            @Mapping(source = "initiatorId", target = "initiator.id"),
            @Mapping(source = "rating", target = "likesCount"),
            @Mapping(target = "category", source = "categoryId"),
            @Mapping(target = "location", source = "locationId")
    })
    EventFullDto eventToEventFullDto(EventDto eventDto);

    // Map the initiator id to a UserShortDto, you can create a separate method for that if necessary
    default UserShortDto mapInitiator(Long initiatorId) {
        return new UserShortDto(initiatorId, "User Name"); // Replace with actual logic to fetch user details
    }

    // Map the categoryId to CategoryDto
    default CategoryDto mapCategory(Long categoryId) {
        return new CategoryDto(categoryId, "Category Name"); // Replace with actual logic to fetch category details
    }

    // Map the locationId to LocationDto
    default LocationDto mapLocation(Long locationId) {
        return new LocationDto(locationId, 40.7128f, -74.0060f, 100L); // Replace with actual logic to fetch location details
    }

    // You might need to implement a method for EventState based on state value
    default EventState mapState(String state) {
        switch (state) {
            case "PENDING":
                return EventState.PENDING;
            case "PUBLISHED":
                return EventState.PUBLISHED;
            case "CANCELED":
                return EventState.CANCELED;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
