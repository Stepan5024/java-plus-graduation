package ru.practicum.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.core.api.dto.event.EventFullDto;
import ru.practicum.core.api.dto.event.EventShortDto;
import ru.practicum.core.api.dto.event.NewEventDto;
import ru.practicum.model.Event;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "category", expression = "java(null)")
    Event newEventDtoToEvent(NewEventDto newEventDto);

    EventFullDto eventToEventFullDto(Event event);

    List<EventShortDto> listEventToListEventShortDto(List<Event> events);

    List<EventFullDto> listEventToListEventFullDto(List<Event> events);
}
