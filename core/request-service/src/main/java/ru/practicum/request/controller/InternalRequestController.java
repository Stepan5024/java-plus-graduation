package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.api.enums.RequestStatus;
import ru.practicum.request.service.RequestService;

import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(InternalRequestController.INTERNAL_REQUESTS_PATH)
public class InternalRequestController {

    public static final String INTERNAL_REQUESTS_PATH = "/internal/requests";
    public static final String COUNT_PATH = "/count";
    public static final String COUNT_BY_EVENT_ID_PATH = COUNT_PATH + "/{eventId}";

    private final RequestService requestService;

    @GetMapping(COUNT_BY_EVENT_ID_PATH)
    public long countByStatusAndEventId(@RequestParam RequestStatus status, @PathVariable long eventId) {
        log.info("|| ==> GET {} Counting by status {} of eventId {}", COUNT_BY_EVENT_ID_PATH, status, eventId);
        long count = requestService.countByStatusAndEventId(status, eventId);
        log.info("|| <== GET {} Returning count by status {} of eventId {}", COUNT_BY_EVENT_ID_PATH, status, eventId);
        return count;
    }

    @GetMapping(COUNT_PATH)
    public Map<Long, Long> countByStatusAndEventsIds(@RequestParam RequestStatus status, @RequestParam List<Long> eventsIds) {
        log.info("|| ==> GET {} Counting by status {} of eventIds {}", COUNT_PATH, status, eventsIds);
        Map<Long, Long> counts = requestService.countByStatusAndEventsIds(status, eventsIds);
        log.info("|| <== GET {} Returning count by status {} of eventIds {}", COUNT_PATH, status, eventsIds);
        return counts;
    }
}