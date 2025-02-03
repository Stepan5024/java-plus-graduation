package ru.practicum.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.ViewStatsDto;
import ru.practicum.core.api.dto.event.*;
import ru.practicum.core.api.dto.request.ParticipationRequestDto;
import ru.practicum.core.api.enums.EventState;
import ru.practicum.core.api.enums.RequestStatus;
import ru.practicum.core.api.enums.StateAction;
import ru.practicum.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.mapper.EventMapper;
import ru.practicum.dto.user.UserDto;
import ru.practicum.enums.EventPublicSort;
import ru.practicum.exception.DataTimeException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RestrictionsViolationException;
import ru.practicum.exchange.CategoryFeignClient;
import ru.practicum.exchange.RequestsFeignClient;
import ru.practicum.exchange.UserFeignClient;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.utility.StatProxyClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.model.QEvent.event;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserFeignClient userFeignClient;
    private final CategoryFeignClient categoryFeignClient;
    private final RequestsFeignClient requestsFeignClient;
    private final StatProxyClient statClient;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, long userId) {
        log.info("The beginning of the process of creating a event");
        UserDto initiatorDto = userFeignClient.getUserById(userId);
        if (initiatorDto == null) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }


        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id " + newEventDto.getCategory() + " not found"));


        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataTimeException("The date and time for which the event is scheduled cannot be " +
                    "earlier than two hours from the current moment");
        }
        log.info("Paid value: {}", newEventDto.getPaid());
        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
            log.info("Paid set to default: false");
        }
        log.info("Request moderation value: {}", newEventDto.getRequestModeration());
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
            log.info("Request moderation set to default: true");
        }
        log.info("Participant limit value: {}", newEventDto.getParticipantLimit());
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
            log.info("Participant limit set to default: 0");
        }

        Event newEvent = eventMapper.newEventDtoToEvent(newEventDto);
        newEvent.setCategory(category);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setInitiator(initiatorDto);
        newEvent.setPublishedOn(LocalDateTime.now());
        newEvent.setState(EventState.PENDING);
        newEvent.setConfirmedRequests(0L);
        newEvent.setRating(0L);

        Event event = eventRepository.save(newEvent);
        EventFullDto eventFullDto = eventMapper.eventToEventFullDto(event);
        eventFullDto.setViews(0L);

        log.info("The event has been created");
        return eventFullDto;
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto findEventById(long userId, long eventId) {
        log.info("The beginning of the process of finding a event");

        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        List<ViewStatsDto> viewStats = getViewStats(List.of(event));

        EventFullDto eventFullDto = eventMapper.eventToEventFullDto(event);

        if (!CollectionUtils.isEmpty(viewStats)) {
            eventFullDto.setViews(viewStats.getFirst().getHits());
        } else {
            eventFullDto.setViews(0L);
        }

        log.info("The event was found");
        return eventFullDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> findEventsByUser(long userId, int from, int size) {
        log.info("The beginning of the process of finding a events");

        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        PageRequest pageRequest = PageRequest.of(from, size);
        BooleanExpression byUserId = event.initiatorId.eq(userId);
        Page<Event> pageEvents = eventRepository.findAll(byUserId, pageRequest);
        List<Event> events = pageEvents.getContent();
        setViews(events);

        List<EventShortDto> eventsShortDto = eventMapper.listEventToListEventShortDto(events);

        log.info("The events was found");
        return eventsShortDto;
    }

    public List<Event> findAllByCategoryId(long categoryId) {
        return eventRepository.findAllByCategoryId(categoryId);
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(UpdateEventUserRequest updateEvent, long userId, long eventId) {
        log.info("The beginning of the process of updates a event");

        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new RestrictionsViolationException("You can only change canceled events or events in the waiting state " +
                    "for moderation");
        }

        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            CategoryDto categoryDto = categoryFeignClient.getCategoryById(updateEvent.getCategory());
            if (categoryDto == null) {
                throw new NotFoundException("Category with id=" + updateEvent.getCategory() + " was not found");
            }
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setName(categoryDto.getName());
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataTimeException("The date and time for which the event is scheduled cannot be " +
                        "earlier than two hours from the current moment");
            } else {
                event.setEventDate(updateEvent.getEventDate());
            }
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
            }
        }

        log.info("The events was update");
        return eventMapper.eventToEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findRequestByEventId(long userId, long eventId) {
        log.info("The beginning of the process of finding requests for userId={} and eventId={}", userId, eventId);

        // Проверка существования пользователя
        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        // Проверка существования события
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        // Получение заявок для события через Feign клиент
        List<ParticipationRequestDto> requests = requestsFeignClient.getRequestsByEventId(eventId);

        // Преобразование сущностей Request в DTO ParticipationRequestDto
        log.info("Found {} requests for eventId={}", requests.size(), eventId);
        return requests;
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> updateRequestByEventId(EventRequestStatusUpdateRequestDto updateRequests,
                                                                long userId,
                                                                long eventId) {
        log.info("The beginning of the process of update a requests");

        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        List<ParticipationRequestDto> confirmedRequests = requestsFeignClient.getConfirmedRequestsByEventId(eventId);


        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == confirmedRequests.size()) {
            throw new RestrictionsViolationException("The limit on applications for this event has been reached, " +
                    "there are " + (event.getParticipantLimit() - event.getConfirmedRequests()) + " free places");
        }

        List<ParticipationRequestDto> requests = requestsFeignClient.getRequestsByIds(updateRequests.getRequestIds());


        if (requests.stream().map(ParticipationRequestDto::getStatus).anyMatch(status -> !status.equals(RequestStatus.PENDING))) {
            throw new RestrictionsViolationException("The status can only be changed for applications that are " +
                    "in the PENDING state");
        }

        requests.forEach(request -> request.setStatus(updateRequests.getStatus()));

        if (updateRequests.getStatus().equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + updateRequests.getRequestIds().size());
        }

        log.info("The requests was updated");
        return requests;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllPublicEvents(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  boolean onlyAvailable, EventPublicSort sort, int from, int size) {
        log.info("The beginning of the process of finding a events by public");

        if ((rangeStart != null) && (rangeEnd != null) && (rangeStart.isAfter(rangeEnd))) {
            throw new DataTimeException("Start time after end time");
        }
        Page<Event> events;
        PageRequest pageRequest = getCustomPage(from, size, sort);
        BooleanBuilder builder = new BooleanBuilder();

        if (text != null) {
            builder.and(event.annotation.containsIgnoreCase(text.toLowerCase())
                    .or(event.description.containsIgnoreCase(text.toLowerCase())));
        }

        if (!CollectionUtils.isEmpty(categories)) {
            builder.and(event.category.id.in(categories));
        }

        if (rangeStart != null && rangeEnd != null) {
            builder.and(event.eventDate.between(rangeStart, rangeEnd));
        } else if (rangeStart == null && rangeEnd != null) {
            builder.and(event.eventDate.between(LocalDateTime.MIN, rangeEnd));
        } else if (rangeStart != null) {
            builder.and(event.eventDate.between(rangeStart, LocalDateTime.MAX));
        }

        if (onlyAvailable) {
            builder.and(event.participantLimit.eq(0L))
                    .or(event.participantLimit.gt(event.confirmedRequests));
        }

        if (builder.getValue() != null) {
            events = eventRepository.findAll(builder.getValue(), pageRequest);
        } else {
            events = eventRepository.findAll(pageRequest);
        }
        setViews(events.getContent());
        log.info("The events was found by public");
        return eventMapper.listEventToListEventShortDto(events.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getPublicEventById(long id) {
        log.info("The beginning of the process of finding a event by public");
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));
        setViews(List.of(event));
        log.info("The event was found by public");
        return eventMapper.eventToEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getAllAdminEvents(List<Long> users, EventState state, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, int from,
                                                int size, boolean sortRating) {
        log.info("The beginning of the process of finding a events by admin");
        Page<Event> pageEvents;
        PageRequest pageRequest;

        if (sortRating) {
            pageRequest = getCustomPage(from, size, EventPublicSort.RATING);
        } else {
            pageRequest = getCustomPage(from, size, null);
        }

        BooleanBuilder builder = new BooleanBuilder();
        if (!CollectionUtils.isEmpty(users) && !users.contains(0L)) {
            builder.and(event.initiatorId.in(users));
        }
        if (state != null) {
            builder.and(event.state.eq(state));
        }
        if (!CollectionUtils.isEmpty(categories) && !categories.contains(0L)) {
            builder.and(event.category.id.in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new DataTimeException("Start time after end time");
            }
            builder.and(event.eventDate.between(rangeStart, rangeEnd));
        } else if (rangeStart == null && rangeEnd != null) {
            builder.and(event.eventDate.between(LocalDateTime.MIN, rangeEnd));
        } else if (rangeStart != null) {
            builder.and(event.eventDate.between(rangeStart, LocalDateTime.MAX));
        }

        if (builder.getValue() != null) {
            pageEvents = eventRepository.findAll(builder.getValue(), pageRequest);
        } else {
            pageEvents = eventRepository.findAll(pageRequest);
        }

        List<Event> events = pageEvents.getContent();
        setViews(events);
        log.info("The events was found by admin");
        return eventMapper.listEventToListEventFullDto(events);
    }

    @Transactional
    @Override
    public EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEvent, long eventId) {
        log.info("The beginning of the process of updates a event by admin");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            CategoryDto categoryDto = categoryFeignClient.getCategoryById(updateEvent.getCategory());
            if (categoryDto == null) {
                throw new NotFoundException("Category with id=" + updateEvent.getCategory() + " was not found");
            }
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setName(categoryDto.getName());

            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataTimeException("The date and time for which the event is scheduled cannot be " +
                        "earlier than two hours from the current moment");
            } else {
                event.setEventDate(updateEvent.getEventDate());
            }
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getStateAction() != null) {
            setStateByAdmin(event, updateEvent.getStateAction());
        }

        log.info("The events was update by admin");
        return eventMapper.eventToEventFullDto(event);
    }

    private void setStateByAdmin(Event event, StateAction stateActionAdmin) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1)) &&
                stateActionAdmin.equals(StateAction.PUBLISH_EVENT)) {
            throw new DataTimeException("The start date of the event to be modified must be no earlier " +
                    "than one hour from the date of publication.");
        }

        if (stateActionAdmin.equals(StateAction.PUBLISH_EVENT)) {
            if (!event.getState().equals(EventState.PENDING)) {
                throw new RestrictionsViolationException("An event can be published only if it is in the waiting state " +
                        "for publication");
            }
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else {
            if (event.getState().equals(EventState.PUBLISHED)) {
                throw new RestrictionsViolationException("AAn event can be rejected only if it has not been " +
                        "published yet");
            }
            event.setState(EventState.CANCELED);
        }

    }

    private PageRequest getCustomPage(int from, int size, EventPublicSort sort) {
        if (sort != null) {
            return switch (sort) {
                case EVENT_DATE -> PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "eventDate"));
                case VIEWS -> PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "views"));
                case RATING -> PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "rating"));
            };
        } else {
            return PageRequest.of(from, size);
        }

    }

    private List<ViewStatsDto> getViewStats(List<Event> events) {
        List<String> url = events.stream()
                .map(event -> "/events/" + event.getId())
                .toList();
        Optional<List<ViewStatsDto>> viewStatsDto = Optional.ofNullable(statClient
                .getStats(LocalDateTime.now().minusYears(20), LocalDateTime.now(), url, true)
        );
        return viewStatsDto.orElse(Collections.emptyList());
    }

    private void setViews(List<Event> events) {
        if (CollectionUtils.isEmpty(events)) {
            return;
        }
        Map<String, Long> mapUriAndHits = getViewStats(events).stream()
                .collect(Collectors.toMap(ViewStatsDto::getUri, ViewStatsDto::getHits));

        for (Event event : events) {
            event.setViews(mapUriAndHits.getOrDefault("/events/" + event.getId(), 0L));
        }
    }
}
