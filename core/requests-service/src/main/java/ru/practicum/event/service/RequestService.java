package ru.practicum.event.service;

import ru.practicum.core.api.enums.RequestStatus;
import ru.practicum.user.dto.ParticipationRequestDto;

import java.util.List;
import java.util.Map;

public interface RequestService {

    List<ParticipationRequestDto> getAllRequests(long userId);

    ParticipationRequestDto addRequest(long userId, long eventId);

    long countByStatusAndEventId(RequestStatus status, long eventId);

    Map<Long, Long> countByStatusAndEventsIds(RequestStatus status, List<Long> eventsIds);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}
