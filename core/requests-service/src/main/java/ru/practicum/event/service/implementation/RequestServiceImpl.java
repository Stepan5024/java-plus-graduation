package ru.practicum.event.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.core.api.client.EventFeignClient;
import ru.practicum.core.api.client.UserFeignClient;
import ru.practicum.core.api.dto.event.EventFullDto;
import ru.practicum.core.api.dto.user.UserDto;
import ru.practicum.core.api.enums.EventState;
import ru.practicum.core.api.enums.RequestStatus;
import ru.practicum.core.api.enums.Status;
import ru.practicum.event.exception.IntegrityViolationException;
import ru.practicum.event.exception.NotFoundException;
import ru.practicum.event.model.Request;
import ru.practicum.event.repository.RequestsRepository;
import ru.practicum.event.service.RequestService;
import ru.practicum.user.dto.ParticipationRequestDto;
import ru.practicum.user.dto.mapper.RequestMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestsRepository requestsRepository;
    private final RequestMapper requestMapper;
    private final UserFeignClient userFeignClient;
    private final EventFeignClient eventFeignClient;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getAllRequests(long userId) {
        UserDto user = userFeignClient.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User with id = " + userId + " not found");
        }
        return requestMapper.listRequestToListParticipationRequestDto(
                requestsRepository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(long userId, long eventId) {
        requestsRepository.findByEventIdAndRequesterId(eventId, userId).ifPresent(
                r -> {
                    throw new IntegrityViolationException(
                            "Request with userId " + userId + " eventId " + eventId + " exists");
                });

        EventFullDto event = eventFeignClient.getById(eventId);
        if (event == null) {
            throw new NotFoundException("Event with id = " + eventId + " not found");
        }
        if (event.getInitiator().id() == userId) {
            throw new IntegrityViolationException("UserId " + userId + " initiates eventId " + eventId);
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new IntegrityViolationException("Event with id = " + eventId + " is not published");
        }

        UserDto userDto = userFeignClient.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User with id = " + userId + " not found");
        }


        List<Request> confirmedRequests = requestsRepository.findAllByStatusAndEventId(Status.CONFIRMED, eventId);
        if ((event.getParticipantLimit() != 0L) && (event.getParticipantLimit() == confirmedRequests.size())) {
            throw new IntegrityViolationException("Request limit exceeded");
        }

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequesterId(userDto.id());
        request.setEventId(eventId);
        if ((event.getParticipantLimit() == 0L) || (!event.isRequestModeration())) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }
        return requestMapper.requestToParticipationRequestDto(requestsRepository.save(request));
    }


    @Override
    public long countByStatusAndEventId(RequestStatus status, long eventId) {
        return requestsRepository.countByStatusAndEventId(status, eventId);
    }

    @Override
    public Map<Long, Long> countByStatusAndEventsIds(RequestStatus status, List<Long> eventsIds) {

        List<Map<String, Long>> list = requestsRepository.countByStatusAndEventsIds(RequestStatus.CONFIRMED.toString(), eventsIds);
        Map<Long, Long> eventRequestsWithStatus = new HashMap<>();
        for (Map<String, Long> row : list) {
            Long eventId = row.get("EVENT_ID");
            Long statusCount = row.get("EVENT_COUNT");
            eventRequestsWithStatus.put(eventId, statusCount);
        }
        return eventRequestsWithStatus;
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        UserDto user = userFeignClient.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User with id = " + userId + " not found");
        }
        Request request = requestsRepository.findById(requestId).orElseThrow(() -> new NotFoundException(
                "Request with id = " + requestId + " not found"
        ));
        request.setStatus(Status.CANCELED);
        return requestMapper.requestToParticipationRequestDto(requestsRepository.save(request));
    }
}
