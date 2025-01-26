package ru.practicum.compilation.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import ru.practicum.compilation.dto.EventDto;
import ru.practicum.compilation.dto.EventFullDto;
import ru.practicum.compilation.dto.EventShortDto;
import ru.practicum.compilation.dto.NewEventDto;
import ru.practicum.compilation.model.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "category", expression = "java(null)")
    Event newEventDtoToEvent(NewEventDto newEventDto);

    EventFullDto eventToEventFullDto(Event event);

    List<EventShortDto> listEventToListEventShortDto(List<Event> events);

    List<EventFullDto> listEventToListEventFullDto(List<Event> events);

    // Добавляем метод для преобразования EventDto в Event
    @Mapping(target = "category", ignore = true) // Категория будет установлена отдельно
    @Mapping(target = "initiator", ignore = true) // Инициатор будет установлен отдельно
    @Mapping(target = "location", ignore = true) // Локация будет установлена отдельно
    @Mapping(target = "state", expression = "java(ru.practicum.compilation.model.State.valueOf(eventDto.getState()))")
    Event fromDto(EventDto eventDto);
}
