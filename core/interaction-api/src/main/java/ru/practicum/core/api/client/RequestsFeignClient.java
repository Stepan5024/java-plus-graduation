package ru.practicum.core.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.api.dto.request.ParticipationRequestDto;
import ru.practicum.core.api.enums.EventState;
import ru.practicum.core.api.enums.RequestStatus;
import ru.practicum.core.api.enums.Status;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "request-service")
public interface RequestsFeignClient {

    @GetMapping("/internal/requests/count/{eventId}")
    long countByStatusAndEventId(@RequestParam RequestStatus status, @PathVariable long eventId);

    @GetMapping("/internal/requests/count")
    Map<Long, Long> countByStatusAndEventsIds(
            @RequestParam RequestStatus status, @RequestParam List<Long> eventsIds);

    @GetMapping("/requests/event/{eventId}")
    List<ParticipationRequestDto> getRequestsByEventId(@PathVariable long eventId);

    @GetMapping("/requests/confirmed/{eventId}")
    List<ParticipationRequestDto> getConfirmedRequestsByEventId(@PathVariable long eventId);

    @PostMapping("/requests/status")
    void updateRequestsStatus(@RequestBody List<Long> requestIds, @RequestParam EventState status);

    @PostMapping("/requests")
    List<ParticipationRequestDto> getRequestsByIds(@RequestBody Set<Long> requestIds);

    @GetMapping("/requests/exists")
    boolean existsByEventAndRequesterAndStatus(@RequestParam("eventId") long eventId,
                                               @RequestParam("requesterId") long requesterId,
                                               @RequestParam("status") Status status);

}
