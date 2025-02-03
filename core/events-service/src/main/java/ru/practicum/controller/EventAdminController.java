package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.api.dto.event.EventFullDto;
import ru.practicum.core.api.dto.event.UpdateEventAdminRequest;
import ru.practicum.core.api.enums.EventState;
import ru.practicum.service.EventService;
import ru.practicum.utility.Constants;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllAdminEvents(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) EventState state,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
                                                LocalDateTime rangeStart,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
                                                LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size,
                                                @RequestParam(defaultValue = "true") Boolean sortRating) {
        log.info("Get all admin events by users {}, state {}, categories {}, rangeStart {}, rangeEnd {}, from {}, " +
                "size {}, sortRating {}", users, state, categories, rangeStart, rangeEnd, from, size, sortRating);
        return eventService.getAllAdminEvents(users, state, categories, rangeStart, rangeEnd, from, size, sortRating);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventAdmin(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest,
                                         @PathVariable long eventId) {
        log.info("Received a PATCH request to update event with an eventId = {} request body {}",
                eventId, updateEventAdminRequest);
        return eventService.updateEventAdmin(updateEventAdminRequest, eventId);
    }
}
