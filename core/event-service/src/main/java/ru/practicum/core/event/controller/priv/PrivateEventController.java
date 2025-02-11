package ru.practicum.core.event.controller.priv;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.api.dto.event.EventFullDto;
import ru.practicum.core.api.dto.event.EventShortDto;
import ru.practicum.core.api.dto.event.NewEventDto;
import ru.practicum.core.api.dto.event.UpdateEventUserRequest;
import ru.practicum.core.event.controller.params.EventGetByIdParams;
import ru.practicum.core.event.controller.params.EventUpdateParams;
import ru.practicum.core.event.controller.params.search.EventSearchParams;
import ru.practicum.core.event.controller.params.search.PrivateSearchParams;
import ru.practicum.core.event.service.EventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("==> POST. /users/{userId}/events " +
                "Creating new event {} by user with id: {}", newEventDto, userId);
        EventFullDto receivedEventDto = eventService.create(userId, newEventDto);
        log.info("<== POST. /users/{userId}/events " +
                "Returning new event {}: {}", receivedEventDto.id(), receivedEventDto);
        return receivedEventDto;
    }

    @GetMapping
    public List<EventShortDto> getAll(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("==> GET. /users/{userId}/events " +
                "Getting all user id {} event: from {}, size {}", userId, from, size);
        EventSearchParams searchParams = new EventSearchParams();
        searchParams.setPrivateSearchParams(new PrivateSearchParams(userId));
        searchParams.setFrom(from);
        searchParams.setSize(size);
        List<EventShortDto> receivedEventsDtoList =
                eventService.getAllByInitiator(searchParams);

        log.info("<== GET. /users/{userId}/events " +
                "Returning all user id {} event: size {}", userId, receivedEventsDtoList.size());
        return receivedEventsDtoList;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable long userId, @PathVariable long eventId) {
        log.info("==> GET. /users/{userId}/events/{eventId} " +
                "Getting event with id: {}, by user with id: {}", eventId, userId);
        EventFullDto receivedEventDto = eventService.getById(new EventGetByIdParams(userId, eventId), null);
        log.info("<== GET. /users/{userId}/events/{eventId} " +
                "Returning event with id: {}", receivedEventDto.id());
        return receivedEventDto;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable long userId,
                               @PathVariable long eventId,
                               @Valid @RequestBody UpdateEventUserRequest updateEventDto) {
        log.info("==> PATCH. /users/{userId}/events/{eventId} " +
                "Updating event with id: {}, by user with id: {}. Updating: {}", eventId, userId, updateEventDto);
        EventFullDto receivedEventDto = eventService.update(
                eventId, new EventUpdateParams(userId, updateEventDto, null));
        log.info("<== PATCH. /users/{userId}/events/{eventId} " +
                "Returning updated event with id: {}, by user with id: {}. Updating: {}",
                eventId, userId, receivedEventDto);
        return receivedEventDto;
    }

}
