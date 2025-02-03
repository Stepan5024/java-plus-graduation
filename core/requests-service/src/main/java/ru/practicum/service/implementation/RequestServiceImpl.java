package ru.practicum.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.core.api.enums.EventState;
import ru.practicum.core.api.enums.Status;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.mapper.RequestMapper;
import ru.practicum.exception.IntegrityViolationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exchange.EventFeignClient;
import ru.practicum.exchange.UserFeignClient;
import ru.practicum.model.Request;
import ru.practicum.repository.RequestsRepository;
import ru.practicum.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

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

        EventDto event = eventFeignClient.getEventById(eventId);
        if (event == null) {
            throw new NotFoundException("Event with id = " + eventId + " not found");
        }
        if (event.getInitiatorId() == userId) {
            throw new IntegrityViolationException("UserId " + userId + " initiates eventId " + eventId);
        }
        if (!event.getState().equals(EventState.PUBLISHED.name())) {
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
        request.setRequesterId(userDto.getId());
        request.setEventId(eventId);
        if ((event.getParticipantLimit() == 0L) || (!event.isRequestModeration())) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }
        return requestMapper.requestToParticipationRequestDto(requestsRepository.save(request));
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
