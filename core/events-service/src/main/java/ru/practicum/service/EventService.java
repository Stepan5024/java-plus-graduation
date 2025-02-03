package ru.practicum.service;


import ru.practicum.core.api.dto.event.*;
import ru.practicum.core.api.dto.request.ParticipationRequestDto;
import ru.practicum.core.api.enums.EventState;
import ru.practicum.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.enums.EventPublicSort;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(NewEventDto newEventDto, long userId);

    EventFullDto findEventById(long userId, long eventId);

    List<EventShortDto> findEventsByUser(long userId, int from, int size);

    EventFullDto updateEvent(UpdateEventUserRequest updateEventUserRequest, long userId, long eventId);

    List<Event> findAllByCategoryId(long categoryId);

    List<ParticipationRequestDto> findRequestByEventId(long userId, long eventId);

    List<ParticipationRequestDto> updateRequestByEventId(EventRequestStatusUpdateRequestDto updateRequest,
                                                             long userId,
                                                             long eventId);

    List<EventShortDto> getAllPublicEvents(String text, List<Long> categories, Boolean paid,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                           boolean onlyAvailable, EventPublicSort sort, int from, int size);

    EventFullDto getPublicEventById(long id);

    List<EventFullDto> getAllAdminEvents(List<Long> users, EventState state, List<Long> categories, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, int from, int size, boolean sortRating);

    EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, long eventId);
}
